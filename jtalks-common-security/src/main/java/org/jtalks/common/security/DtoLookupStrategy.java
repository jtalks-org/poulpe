package org.jtalks.common.security;


import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>This implementation of the {@link LookupStrategy} is very similar to
 * {@link org.springframework.security.acls.jdbc.BasicLookupStrategy},
 * except for object identity classes are being replaced with their mappings, if any.</p>
 * <p>During the processing of the list of object identities, we check, if class, stored in the {@code type} field
 * of the identity, is mapped to another class, and if it does, replaces the identity with the new one with same
 * identifier and mapped class.</p>
 * <p>This is used in order to apply security records created for model classes
 * to the DTOs related to these classes.</p>
 * See {@link DtoLookupStrategy#readAclsById(List, List)} for details.
 * <p/>
 * Date: 16.09.2011<br />
 * Time: 15:07
 *
 * @author Alexey Malev
 */
public class DtoLookupStrategy implements LookupStrategy {

    private DtoMapper mapper;
    private LookupStrategy lookupStrategy;

    /**
     * Default constructor for the strategy.
     *
     * @param lookupStrategy Lookup Strategy used to delegate lookup.
     * @param mapper         Mapper used to retrieve mapped model classes.
     */
    public DtoLookupStrategy(LookupStrategy lookupStrategy, DtoMapper mapper) {
        this.lookupStrategy = lookupStrategy;
        this.mapper = mapper;
    }

    /**
     * <p>This method looks through all provided objects and replace them with their mappings, if ones exists,
     * before further processing performed by BasicLookupStrategy.</p>
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) {
        //first, we create an empty list for the identities possibly mapped to DTOs
        List<ObjectIdentity> mappedObjects = new ArrayList<ObjectIdentity>(objects.size());
        //reverse mapping: model entity-related identities -> set of DTO-related identities
        Map<ObjectIdentity, Set<ObjectIdentity>> usedMapping = new HashMap<ObjectIdentity, Set<ObjectIdentity>>();
        for (ObjectIdentity objectIdentity : objects) {
            ObjectIdentity mappedIdentity = getMappedIdentity(objectIdentity);

            //save original mapping - set of all classes that is mapped to this model entity class
            Set<ObjectIdentity> mappedToModelClasses = usedMapping.get(mappedIdentity);
            if (mappedToModelClasses == null) {
                mappedToModelClasses = new HashSet<ObjectIdentity>();
            }
            mappedToModelClasses.add(objectIdentity);
            usedMapping.put(mappedIdentity, mappedToModelClasses);

            mappedObjects.add(mappedIdentity);
        }

        //get a map [mapped_identity -> acl] from BaseLookupStrategy
        return restoreOriginalIdentities(usedMapping, this.lookupStrategy.readAclsById(mappedObjects, sids));
    }

    /**
     * This method restores original identities basing on mapping of "model" identities.
     *
     * @param usedMapping      Mapping of "model" identities to DTO identities.
     * @param mappedIdentities Map of identities to Acls, in which identities needs to be replaced.
     * @return Map, similar to <code>mappedIdentities</code> - "model" identities will be replaced with DTO identities.
     */
    private static Map<ObjectIdentity, Acl> restoreOriginalIdentities(
            Map<ObjectIdentity, Set<ObjectIdentity>> usedMapping, Map<ObjectIdentity, Acl> mappedIdentities) {
        Map<ObjectIdentity, Acl> acls = new HashMap<ObjectIdentity, Acl>();
        for (Map.Entry<ObjectIdentity, Acl> mappedIdentittiesEntry : mappedIdentities.entrySet()) {
            for (ObjectIdentity mappedToThisIdentity : usedMapping.get(mappedIdentittiesEntry.getKey())) {
                acls.put(mappedToThisIdentity, mappedIdentittiesEntry.getValue());
            }
        }
        return acls;
    }

    /**
     * This method returns {@link ObjectIdentity} mapped to provided one using the following logic:
     * <ul>
     * <li>If no mapping found for the identity type, same object is returned;</li>
     * <li>Instead, a new {@link ObjectIdentity} is created with the type mapped to the type of the original
     * identity and with the same identifier.</li>
     * </ul>
     *
     * @param identity Original identity
     * @return Mapped identity as described above.
     */
    private ObjectIdentity getMappedIdentity(ObjectIdentity identity) {
        ObjectIdentity result = identity;

        String identityClass = identity.getType();
        Class identityMappedTo = mapper.getMapping(identityClass);
        if (identityMappedTo != null) {
            result = new ObjectIdentityImpl(identityMappedTo.getCanonicalName(), identity.getIdentifier());
        }

        return result;
    }
}


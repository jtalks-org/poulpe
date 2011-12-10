package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.Entity;

public class Group  extends Entity{
    private String name;
    private String description;
    
    public Group() {
        // TODO Auto-generated constructor stub
    }
    
    public Group(String name, String description){
        this();
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}

package org.jtalks.poulpe.web.controller.userbanning;

import java.util.List;
import javax.jws.soap.SOAPBinding.Use;

import org.jtalks.common.model.entity.User;

public interface UserBanningView{
    public void updateView(List<User> users);
    public void clearView();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.exception.BaseException;
import java.util.List;

/**
 *
 * @author akshat666
 */
public interface PushBO {
    
    public void hopNotificationToListOfAuthIDs(List authIDs, long pickupID, String requesterName, String dropAddress) throws BaseException;
    
}

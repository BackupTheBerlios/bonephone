/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: Marc Bednarek (bednarek@nist.gov)                                    *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/

package gov.nist.sip.stack;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Enumeration;

public interface MessageLogTable extends Remote {

    public String keys() throws RemoteException;

    public String flush(String key) throws RemoteException;

}

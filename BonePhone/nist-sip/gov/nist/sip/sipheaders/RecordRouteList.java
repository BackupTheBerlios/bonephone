/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
* See ../../../../doc/uncopyright.html for conditions of use.                  *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Modified By:  O. Deruelle (deruelle@nist.gov), added JAVADOC                 *                                                                                  
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.sipheaders;
import gov.nist.sip.*;
import java.util.ListIterator;

/**
* RecordRoute List of SIP headers (a collection of Addresses)
* @since 0.9
* @version 1.0 
*/
public class RecordRouteList extends SIPHeaderList {
    
        /** Default constructor
         */    
	public RecordRouteList() {
	 	super("RecordRouteList", 
			SIPHEADERS_PACKAGE + ".RecordRoute", RECORD_ROUTE);
	}

	
       /** Get the route list for this record route list.
	* Returns the reverse of this list of record-route headers.
	*
	*@return the route list for this record route list.
	*/
       public RouteList getRouteList() {
              RouteList rlist = new RouteList();
              ListIterator li = this.listIterator(rlist.size() + 1);
              while (li.hasPrevious()) {
                RecordRoute rr = (RecordRoute) li.previous();
                Route route = new Route();
                route.setAddress(rr.getAddress());
                route.setParams(rr.getParams());
		rlist.add(route);
              }
              return rlist;
       }
        
}

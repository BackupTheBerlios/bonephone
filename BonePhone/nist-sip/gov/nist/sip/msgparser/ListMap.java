/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD)         *
* See ../../../../doc/uncopyright.html for conditions of use                   *
* Author: M. Ranganathan (mranga@nist.gov)                                     *
* Questions/Comments: nist-sip-dev@antd.nist.gov                               *
*******************************************************************************/
package gov.nist.sip.msgparser;
import gov.nist.sip.sipheaders.*;
import gov.nist.sip.*;
import java.util.Hashtable;

/**
* A map of which of the standard headers may appear as a list 
*/

class ListMap  implements PackageNames  {
       // A table that indicates whether a header has a list representation or
       // not (to catch adding of the non-list form when a list exists.)
       // Entries in this table allow you to look up the list form of a header
       // (provided it has a list form).
       private static Hashtable		headerListTable;
       private static boolean initialized;
       static { initializeListMap();  }


       static private void initializeListMap() {
	    /* 
	    * Build a table mapping between objects that have a list form
	    * and the class of such objects.
	    */
	    headerListTable = new Hashtable();
	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE +".ExtensionHeader"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ExtensionHeaderList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE +".Contact"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ContactList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ContentEncoding"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ContentEncodingList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Via"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ViaList"));
	
	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".WWWAuthenticate"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".WWWAuthenticateList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Accept"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AcceptList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AcceptEncoding"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AcceptEncodingList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AcceptLanguage"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AcceptLanguageList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ProxyRequire"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ProxyRequireList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Route"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".RouteList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Require"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".RequireList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Warning"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".WarningList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Unsupported"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".UnsupportedList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AlertInfo"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AlertInfoList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".CallInfo"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".CallInfoList"));

	    headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ProxyAuthenticate"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE +".ProxyAuthenticateList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Allow"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AllowList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".RecordRoute"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".RecordRouteList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ContentLanguage"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ContentLanguageList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ErrorInfo"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".ErrorInfoList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Supported"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".SupportedList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".InReplyTo"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".InReplyToList"));

	   headerListTable.put(
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".Also"),
		GenericObject.getClassFromName
			(SIPHEADERS_PACKAGE + ".AlsoList"));

	    
	    initialized = true;

	}

	/**
	* return true if this has an associated list object.
	*/
	static  protected boolean hasList(SIPHeader sipHeader) {
		if (sipHeader instanceof SIPHeaderList) return false;
	        else {
			Class headerClass = sipHeader.getClass();
			return headerListTable.get(headerClass) != null;
		}
	}

	/**
	* Return true if this has an associated list object.
	*/
	static  protected boolean hasList(Class sipHdrClass) {
		if (!initialized) initializeListMap();
		return headerListTable.get(sipHdrClass) != null;
	}

	/**
	* Get the associated list class.
	*/
	static protected Class getListClass(Class sipHdrClass) {
                if (!initialized) initializeListMap();
		return (Class) headerListTable.get(sipHdrClass);
	}

	/**
	* Return a list object for this header if it has an associated
	* list object.
	*/
	static protected 
		SIPHeaderList getList(SIPHeader sipHeader) {
		if (!initialized) initializeListMap();
		try {
		   Class headerClass = sipHeader.getClass();
		   Class listClass = (Class) headerListTable.get(headerClass);
		   return (SIPHeaderList) listClass.newInstance();
		} catch (InstantiationException ex) {
		    InternalError.handleException(ex);
		} catch (IllegalAccessException ex)  {
		    InternalError.handleException(ex);
		}
		return  null;
	}

}

package traceviewer ;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.* ;


public class TraceFileParser{

	InputSource xmlSource = null ;

	
	public TraceFileParser(String strSource){
		 byte[] bytes = strSource.getBytes();
                InputStream input = new ByteArrayInputStream(bytes);
		xmlSource = new InputSource(input) ;
	}

	public TraceFileParser(InputStream streamSource){
	    xmlSource = new InputSource(streamSource) ;
	}
	
        public void setXMLsource(String strSource){
	    byte[] bytes = strSource.getBytes();
	    InputStream input = new ByteArrayInputStream(bytes);
	    xmlSource = new InputSource(input) ;
	}

        public void setXMLsource(InputStream streamSource){
	    xmlSource = new InputSource(streamSource) ;
	}

	public TraceSession parse() throws SAXException , IOException{
		
		TraceSession traceSession = new TraceSession() ;			

		DOMParser parser = new DOMParser();
		parser.parse(xmlSource);
		
		Node messagesNode = parser.getDocument().getFirstChild() ;
		NodeList nodeList = messagesNode.getChildNodes() ;

		TraceMessage traceMessage = null ;

		for(int i = 0 ; i < nodeList.getLength() ; i++){
			
			traceMessage = new TraceMessage() ;
				
			// We have a message
			if(nodeList.item(i).getNodeName().equals("message")){
					
			// Get the attributes
			NamedNodeMap messageNodeMap = nodeList.item(i).getAttributes() ;
			if(messageNodeMap.getLength() == 4){

				traceMessage.setFrom(messageNodeMap.getNamedItem("from").getNodeValue()) ;
						
				traceMessage.setTo(messageNodeMap.getNamedItem("to").getNodeValue()) ;

				traceMessage.setTime(messageNodeMap.getNamedItem("time").getNodeValue()) ;

				traceMessage.setFirstLine(messageNodeMap.getNamedItem("firstLine").getNodeValue()) ;
			}
			else{
				System.out.println("Problem ...") ;
				return null ;					
			}
				
			//Get the Text of the message
			NodeList messageNodeList = nodeList.item(i).getChildNodes() ;
					
			if(messageNodeList.getLength() > 0){
				for(int j = 0 ; j < messageNodeList.getLength() ; j++)
					if(messageNodeList.item(j).getNodeType() == Node.CDATA_SECTION_NODE){
						traceMessage.setMessageString(messageNodeList.item(j).getNodeValue()) ;
					}
						
					}else{
						System.out.println("xml File problem\n") ;
						return null ;
					}
		
				traceSession.add(traceMessage) ;

			}
		}
		
		return traceSession ;
	} // end parse


} // end class

package eml.studio.server.graph;

import eml.studio.shared.graph.OozieGraph;
import eml.studio.shared.graph.OozieProgramNode;
import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import eml.studio.shared.graph.OozieDatasetNode;

// unable to run
public class OozieGraphXMLParserTest extends TestCase {

	static SAXReader reader = new SAXReader();


  public void testParse() throws DocumentException {
	  Document doc = reader.read("test/bda/studio/server/graph/graph.xml");
	  String xml = doc.asXML();
    OozieGraph graph = OozieGraphXMLParser.parse(xml);
    assertEquals(graph.getDatasetNodes().size(), 1);
    
    OozieDatasetNode data = graph.getDatasetNodes().get(0);
    assertEquals(data.getId(), "cadata-154139996b5-50de");
    assertEquals(data.getX(), 408);
    assertEquals(data.getY(), 236);
    assertEquals(data.getFile(), "/EML/Data/18F89E50-92EE-4B94-8038-D50455B66E8F");
    
    OozieProgramNode node = graph.getProgramNodes().get(0);
    assertEquals( node.getParams().get(0), "9:Variance");
    assertEquals( node.getId(), "CART_Train-15433a3df8f-760e");
    assertEquals(graph.getProgramNodes().size(), 7);
    
    assertEquals(graph.getEdges().size(), 9);
  }
}
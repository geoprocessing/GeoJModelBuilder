import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import cn.edu.whu.geos.wls.x10.WorkflowInstanceDocument;
import cn.edu.whu.geos.wls.x10.WorkflowInstanceType;


public class ReadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File xmlFile = new File("D:/Workspace/workflow/water_extraction_instance.xml");

		// Bind the instance to the generated XMLBeans types.
		
		
		try {
			//可以直接从XML流中读取
			WorkflowInstanceDocument empDoc = WorkflowInstanceDocument.Factory.parse(xmlFile);
			WorkflowInstanceType wlstype= empDoc.getWorkflowInstance();
//			ExecuteRequestType exeReqType = empDoc.getExecute();
		} catch (XmlException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get and print pieces of the XML instance.
		
		
		System.out.println("test");
	}

}

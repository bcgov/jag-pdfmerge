package ca.bc.gov.open.jag.documentutils.adobe;

import ca.bc.gov.open.jag.documentutils.adobe.models.MergeDoc;
import org.w3c.dom.Document;

import java.util.LinkedList;

public interface DDXService {

    Document createMergeDDX(LinkedList<MergeDoc> pageList, boolean addToC);

    com.adobe.idp.Document convertDDX(Document ddx);

}

package syntaxCheckerPanel;

import java.awt.AWTEvent;

public class SyntaxCheckerPanelEvent extends AWTEvent {
	//define id's for events to be associated with pane like the following example
	public static final int event = AWTEvent.RESERVED_ID_MAX + 1;
	//the field id will store the id for any event, so that it can be identified
	public int paneEventID;
	public SyntaxCheckerPanelEvent(SyntaxCheckerPanel source, int id, String paramName)
	{
		super(source, id);
		this.paneEventID = id;
	}
}

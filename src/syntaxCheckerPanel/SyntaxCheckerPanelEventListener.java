package syntaxCheckerPanel;

import java.util.EventListener;

public interface SyntaxCheckerPanelEventListener extends EventListener {
	//methods for types of PaneEvents
	public void eventAction(SyntaxCheckerPanelEvent event);
}

package org.jtalks.poulpe.web.controller.section;

import org.jtalks.poulpe.web.controller.DialogManager;
import org.mockito.ArgumentMatcher;

public class PerformableMatcher extends
		ArgumentMatcher<DialogManager.Performable> {
	
	DialogManager.Performable perf;
	
	public PerformableMatcher(DialogManager.Performable per) {
		this.perf = per;
	}
	
	@Override
	public boolean matches(Object argument) {
		if(!(argument instanceof DialogManager.Performable)){
			return false;
		}
		return true;
		
	}
}

/*
 * generated by Xtext 2.32.0
 */
package org.xtext.example.tablefork.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.util.Modules2;
import org.xtext.example.tablefork.TableForkRuntimeModule;
import org.xtext.example.tablefork.TableForkStandaloneSetup;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class TableForkIdeSetup extends TableForkStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new TableForkRuntimeModule(), new TableForkIdeModule()));
	}
	
}

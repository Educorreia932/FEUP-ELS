package pt.up.fe.els2023;

import com.google.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.parser.IParser;

import org.xtext.example.tablefork.TableForkStandaloneSetup;
import org.xtext.example.tablefork.tableFork.Load;
import org.xtext.example.tablefork.tableFork.Program;
import org.xtext.example.tablefork.tableFork.impl.ProgramImpl;

import java.io.IOException;
import java.util.Collections;

public class Parser {
    @Inject
    private IParser parser;

    public Parser() {
        var injector = new TableForkStandaloneSetup().createInjectorAndDoEMFRegistration();

        injector.injectMembers(this);
    }

    public void parse(String filePath) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource resource = resourceSet.createResource(URI.createFileURI(filePath));

        try {
            resource.load(Collections.emptyMap());
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }

        var treeIterator = resource.getAllContents();
        
        while (treeIterator.hasNext()) {
            var element = treeIterator.next();

            if (element instanceof Program program) {
                System.out.println(program.getLoad());
                System.out.println(program.getOperations());
            }
        }
    }
}

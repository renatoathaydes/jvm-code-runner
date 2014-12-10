package com.athaydes.coderunner.server;

import com.athaydes.coderunner.CodeRunner;
import com.athaydes.coderunner.CompositeCodeRunner;
import com.athaydes.coderunner.LanguageNotAvailableException;
import com.athaydes.coderunner.RemoteException;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component(propagation = true)
@Provides
public class ServerCodeRunner implements CompositeCodeRunner {

    private final Map<String, CodeRunner> codeRunners = new HashMap<>();

    @Override
    public String runScript(String language, String script)
            throws LanguageNotAvailableException, RemoteException {
        CodeRunner runner = codeRunners.get(language);
        if (runner == null) {
            throw new LanguageNotAvailableException("Language not available: " + language);
        }
        try {
            return "" + runner.runScript(script);
        } catch (Exception e) {
            throw new RemoteException(e.toString());
        }
    }

    @Override
    public Set<String> getLanguages() {
        return codeRunners.keySet();
    }

    @Bind(aggregate = true)
    public void bindCodeRunner(CodeRunner codeRunner) {
        System.out.println("Binding code runner: " + codeRunner.getLanguage());
        codeRunners.put(codeRunner.getLanguage(), codeRunner);
    }

    @Unbind
    public void unbindCodeRunner(CodeRunner codeRunner) {
        System.out.println("Unbinding code runner: " + codeRunner.getLanguage());
        codeRunners.remove(codeRunner.getLanguage());
    }

}

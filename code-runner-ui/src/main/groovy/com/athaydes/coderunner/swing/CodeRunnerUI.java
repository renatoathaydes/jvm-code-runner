package com.athaydes.coderunner.swing;

import com.athaydes.coderunner.swing.view.MainView;
import com.athaydes.coderunner.CompositeCodeRunner;
import com.athaydes.coderunner.LanguageNotAvailableException;
import com.athaydes.coderunner.RemoteException;
import org.apache.felix.ipojo.annotations.*;

import java.util.Set;

@Component(name = "CodeRunnerUI")
@Instantiate
public class CodeRunnerUI {

    private MainView mainView;

    public CodeRunnerUI(@Requires CompositeCodeRunner codeRunner) {
        mainView = new MainView(new SafeCodeRunner(codeRunner));
    }

    @Validate
    public void start() {
        mainView.create();
    }

    @Invalidate
    public void stop() {
        if (mainView != null)
            mainView.destroy();
    }

}

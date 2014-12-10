package com.athaydes.coderunner.swing

import com.athaydes.coderunner.CompositeCodeRunner
import com.athaydes.coderunner.LanguageNotAvailableException
import com.athaydes.coderunner.RemoteException
import groovy.transform.CompileStatic

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@CompileStatic
class SafeCodeRunner {

    final CompositeCodeRunner delegate
    final ExecutorService executor = Executors.newSingleThreadExecutor()

    SafeCodeRunner( CompositeCodeRunner delegate ) {
        this.delegate = delegate
        assert delegate
    }

    void runScript( String language, String script, Closure onSuccess, Closure onFailure ) {
        executor.execute {
            try {
                onSuccess this.delegate.runScript( language, script )
            } catch ( LanguageNotAvailableException | RemoteException e ) {
                onFailure e.message
            } catch ( Exception e ) {
                onFailure e.toString()
            }
        }
    }

    void getLanguages( Closure onSuccess, Closure onFailure ) {
        try {
            onSuccess delegate.languages
        } catch ( e ) {
            println "Could not get languages due to $e"
            onFailure Collections.emptySet()
        }
    }


}

package com.athaydes.coderunner.swing.view

import com.athaydes.coderunner.CompositeCodeRunner
import org.junit.Test

/**
 *
 */
class MainViewTest {

    @Test
    void test1() {
        // just for compiling
    }

    public static void main( String[] args ) {
        final view = new MainView( new CompositeCodeRunner() {
            @Override
            String runScript( String language, String script ) {
                return "$language -> $script"
            }

            @Override
            Set<String> getLanguages() {
                [ "Java", "Scala", "Ceylon" ] as Set
            }
        } )
        view.create()
    }

}

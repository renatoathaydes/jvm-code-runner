package com.athaydes.coderunner.swing.view

import com.athaydes.coderunner.swing.SafeCodeRunner
import groovy.beans.Bindable
import groovy.swing.SwingBuilder

import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JTextArea
import java.awt.Color
import java.awt.Dimension

class MainView {

    @Bindable
    class Model {
        String language
    }

    private final builder = new SwingBuilder()
    private final SafeCodeRunner codeRunner
    private final Model model = new Model()

    private JFrame theFrame
    private JComboBox langsCombo = null
    private JTextArea resultText = null

    MainView( SafeCodeRunner codeRunner ) {
        this.codeRunner = codeRunner
    }

    void create() {
        destroy()
        builder.edt {
            theFrame = frame( title: 'D-OSGi IPojo Demo', show: true,
                    size: [ 500, 500 ] as Dimension, locationRelativeTo: null ) {
                vbox {
                    vstrut 10
                    def sourceArea = textArea( editable: true )
                    vstrut 10
                    hbox {
                        hstrut 5
                        button( text: 'Run',
                                enabled: bind { model.language != null },
                                actionPerformed: {
                                    codeRunner.runScript model.language, sourceArea.text,
                                            { result -> setText result, Color.black },
                                            { failure -> setText failure, Color.red }
                                } )
                        hstrut 5
                        label 'Language:'
                        hstrut 5
                        langsCombo = comboBox( actionPerformed: { event ->
                            model.language = event.source.selectedItem
                        } )
                        hstrut 5
                        button( text: 'Update Languages',
                                actionPerformed: { updateLanguages() } )
                        hstrut 5
                    }
                    vstrut 10
                    resultText = textArea( editable: false, background: new Color( 240, 230, 140 ) )
                }
            }
        }

        updateLanguages()
    }

    void destroy() {
        theFrame?.dispose()
    }

    private void setText( text, color ) {
        builder.edt {
            resultText.foreground = color
            resultText.text = text
        }
    }

    private void updateLanguages() {
        if ( langsCombo ) {
            def item = langsCombo.selectedItem
            langsCombo.removeAllItems()
            codeRunner.getLanguages( { languages ->
                languages.toList().sort().each { String lang ->
                    langsCombo.addItem( lang )
                }
                if ( item ) langsCombo.selectedItem = item
            }, { failure ->
                setText 'NO LANGUAGES AVAILABLE!', Color.red
            } )

        }
    }

}

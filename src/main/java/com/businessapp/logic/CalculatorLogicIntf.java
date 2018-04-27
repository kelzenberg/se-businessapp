package com.businessapp.logic;

import com.businessapp.ControllerIntf;

import static com.businessapp.fxgui.CalculatorGUI_Intf.*;

import javafx.beans.property.SimpleStringProperty;


/**
 * Public interface of Calculator logic.
 */
public interface CalculatorLogicIntf extends ControllerIntf {

    final SimpleStringProperty DISPLAY = new SimpleStringProperty();
    final SimpleStringProperty SIDEAREA = new SimpleStringProperty();
    final int DISPLAY_MAXDIGITS = 16;
    final double VAT_RATE = 19.0;        // 19% VAT in Germany

    /**
     * Factory method that returns a new calculator logic controller instance.
     *
     * @return new instance of CalculatorLogic.
     */
    static ControllerIntf getController() {
        return new CalculatorLogic();
    }

    final Object[][] ShortCutKeys = new Object[][]{
            {"\r", Token.K_EQ},
            {"\b", Token.K_BACK},
            {"c", Token.K_C},
            {"e", Token.K_CE},
            {".", Token.K_DOT},
            {"m", Token.K_VAT},
            {"t", Token.K_1000},
    };


    /**
     * Method invoked by user interface to pass next token when user presses a the
     * key pad.
     *
     * @param tok Token of the pressed key/button on the calculators input panel.
     */
    public void nextToken(Token tok);

}

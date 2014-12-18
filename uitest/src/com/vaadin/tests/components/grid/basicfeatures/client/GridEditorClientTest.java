/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.grid.basicfeatures.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.tests.components.grid.basicfeatures.GridBasicClientFeaturesTest;
import com.vaadin.tests.components.grid.basicfeatures.GridBasicFeatures;

public class GridEditorClientTest extends GridBasicClientFeaturesTest {

    @Before
    public void setUp() {
        openTestURL();
        selectMenuPath("Component", "Editor", "Enabled");
    }

    @Test
    public void testProgrammaticOpeningClosing() {
        selectMenuPath("Component", "Editor", "Edit row 5");
        assertNotNull(getEditor());

        selectMenuPath("Component", "Editor", "Cancel edit");
        assertNull(getEditor());
        assertEquals("Row 5 edit cancelled",
                findElement(By.className("grid-editor-log")).getText());
    }

    @Test
    public void testProgrammaticOpeningWithScroll() {
        selectMenuPath("Component", "Editor", "Edit row 100");
        assertNotNull(getEditor());
    }

    @Test(expected = NoSuchElementException.class)
    public void testVerticalScrollLocking() {
        selectMenuPath("Component", "Editor", "Edit row 5");
        getGridElement().getCell(200, 0);
    }

    @Test
    public void testKeyboardOpeningClosing() {

        getGridElement().getCell(4, 0).click();

        new Actions(getDriver()).sendKeys(Keys.ENTER).perform();

        assertNotNull(getEditor());

        new Actions(getDriver()).sendKeys(Keys.ESCAPE).perform();
        assertNull(getEditor());
        assertEquals("Row 4 edit cancelled",
                findElement(By.className("grid-editor-log")).getText());

        // Disable editor
        selectMenuPath("Component", "Editor", "Enabled");

        getGridElement().getCell(5, 0).click();
        new Actions(getDriver()).sendKeys(Keys.ENTER).perform();
        assertNull(getEditor());
    }

    @Test
    public void testWidgetBinding() throws Exception {
        selectMenuPath("Component", "Editor", "Edit row 100");
        WebElement editor = getEditor();

        List<WebElement> widgets = editor.findElements(By
                .className("gwt-TextBox"));

        assertEquals(GridBasicFeatures.COLUMNS, widgets.size());

        assertEquals("(100, 0)", widgets.get(0).getAttribute("value"));
        assertEquals("(100, 1)", widgets.get(1).getAttribute("value"));
        assertEquals("(100, 2)", widgets.get(2).getAttribute("value"));

        assertEquals("100", widgets.get(7).getAttribute("value"));
        assertEquals("<b>100</b>", widgets.get(9).getAttribute("value"));
    }

    @Test
    public void testWithSelectionColumn() throws Exception {
        selectMenuPath("Component", "State", "Selection mode", "multi");
        selectMenuPath("Component", "State", "Editor", "Edit row 5");

        WebElement editor = getEditor();
        List<WebElement> selectorDivs = editor.findElements(By
                .cssSelector("div"));

        assertTrue("selector column cell should've been empty", selectorDivs
                .get(0).getAttribute("innerHTML").isEmpty());
        assertFalse("normal column cell shoul've had contents", selectorDivs
                .get(1).getAttribute("innerHTML").isEmpty());
    }

    @Test
    public void testSave() {
        selectMenuPath("Component", "Editor", "Edit row 100");

        WebElement textField = getEditor().findElements(
                By.className("gwt-TextBox")).get(0);

        textField.clear();
        textField.sendKeys("Changed");

        WebElement saveButton = getEditor().findElement(
                By.className("v-editor-row-save"));

        saveButton.click();

        assertEquals("Changed", getGridElement().getCell(100, 0).getText());
    }

    @Test
    public void testProgrammaticSave() {
        selectMenuPath("Component", "Editor", "Edit row 100");

        WebElement textField = getEditor().findElements(
                By.className("gwt-TextBox")).get(0);

        textField.clear();
        textField.sendKeys("Changed");

        selectMenuPath("Component", "Editor", "Save");

        assertEquals("Changed", getGridElement().getCell(100, 0).getText());
    }
}

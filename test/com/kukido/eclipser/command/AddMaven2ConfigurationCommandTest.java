package com.kukido.eclipser.command;

import com.intellij.execution.RunManager;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.testFramework.LightIdeaTestCase;

public class AddMaven2ConfigurationCommandTest extends LightIdeaTestCase {

    public static final String TEST_CASE_MODULE_NAME = "light_idea_test_case";

    private AddMaven2ConfigurationCommand command;

    // todo: add test cases


    @Override
    protected void tearDown() throws Exception {
        RunManagerImpl manager = (RunManagerImpl) RunManager.getInstance(getProject());
        manager.clearAll();
        super.tearDown();
    }
}

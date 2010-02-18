package com.github.wolfie.detachedtabs;

import java.util.Random;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class DetachedtabsApplication extends Application {
  
  @Override
  public void init() {
    final Window mainWindow = new Window("Disconnectedtabs Application");
    setMainWindow(mainWindow);
    mainWindow.setContent(new CssLayout());
    mainWindow.getContent().setSizeFull();
    
    final CssLayout body = new CssLayout();
    body.setSizeFull();
    
    final DetachedTabs detachedTabs = new DetachedTabs.Horizontal(body);
    
    final HorizontalLayout header = new HorizontalLayout();
    final Label logo = new Label("LOGO!");
    final Label right = new Label("User Name | Logout");
    header.addComponent(logo);
    header.addComponent(detachedTabs);
    header.addComponent(right);
    header.setWidth("100%");
    header.setComponentAlignment(right, Alignment.MIDDLE_RIGHT);
    right.setSizeUndefined();
    
    mainWindow.addComponent(header);
    mainWindow.addComponent(body);
    
    detachedTabs.addTab(getRandomTable(), "Tab 1");
    detachedTabs.addTab(getRandomTable(), "Tab 2");
    detachedTabs.addTab(getRandomTable(), "Tab 3");
    detachedTabs.addTab(getVerticalExample(), "Vertical");
  }
  
  private Component getRandomTable() {
    final Table table = new Table();
    table.addContainerProperty("#1", Integer.class, null);
    table.addContainerProperty("#2", Integer.class, null);
    table.addContainerProperty("#3", Integer.class, null);
    
    final Random rand = new Random();
    for (int i = 0; i < 50; i++) {
      table.addItem(new Integer[] { rand.nextInt(), rand.nextInt(),
          rand.nextInt() }, new Object());
    }
    
    table.setSizeFull();
    return table;
  }
  
  private Component getVerticalExample() {
    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();
    
    final CssLayout cssLayout = new CssLayout();
    cssLayout.setSizeFull();
    
    final DetachedTabs verticalTabs = new DetachedTabs.Vertical(cssLayout);
    verticalTabs.addTab(getRandomTable(), "Tab 1");
    verticalTabs.addTab(getRandomTable(), "Tab 2");
    verticalTabs.addTab(getRandomTable(), "Tab 3");
    verticalTabs.setWidth("100px");
    
    layout.addComponent(verticalTabs);
    layout.addComponent(cssLayout);
    layout.setExpandRatio(cssLayout, 1.0f);
    return layout;
  }
}

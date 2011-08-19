package com.github.wolfie.detachedtabs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * <p>
 * A sort of {@link TabSheet}, with the tabs detached from its sheet.
 * </p>
 * 
 * <p>
 * This component is merely the tabs for a tabsheet. The sheet itself is an
 * external object. These two may be placed anywhere, independent of each other.
 * </p>
 * 
 * <p>
 * This mechanism allows for a more flexible layouts, and you may place other
 * widgets right alongside it, and are not constrained by the "bounding box"
 * presented by the <tt>TabSheet</tt>'s tabs+sheet combination.
 * </p>
 * 
 * @author Henrik Paul
 * @license Apache 2.0
 */
@SuppressWarnings("serial")
public abstract class DetachedTabs extends CustomComponent {

  public static class Horizontal extends DetachedTabs {
    /**
     * Creates a new horizontal {@link DetachedTabs}.
     * 
     * @param componentContainer
     *          The {@link ComponentContainer} that will act as
     *          "the sheet for the tabs". The client code will lose all control
     *          over the <tt>componentContainer</tt>, excepting where it will be
     *          added. So, you may add it to any layout you want, but you are
     *          not in control over what will be presented inside it anymore.
     */
    public Horizontal(final ComponentContainer componentContainer) {
      super(componentContainer, Orientation.HORIZONTAL);
    }
  }

  public static class Vertical extends DetachedTabs {
    /**
     * Creates a new vertical {@link DetachedTabs}.
     * 
     * @param componentContainer
     *          The {@link ComponentContainer} that will act as
     *          "the sheet for the tabs". The client code will lose all control
     *          over the <tt>componentContainer</tt>, excepting where it will be
     *          added. So, you may add it to any layout you want, but you are
     *          not in control over what will be presented inside it anymore.
     */
    public Vertical(final ComponentContainer componentContainer) {
      super(componentContainer, Orientation.VERTICAL);
    }
  }

  private class TabChangeListener implements ClickListener {
    public void buttonClick(final ClickEvent event) {
      final Button button = event.getButton();
      final Component componentToSwitch = buttonComponentMap.get(button);
      switchTo(componentToSwitch, button);
    }
  }

  public enum Orientation {
    HORIZONTAL, VERTICAL
  };

  private final AbstractOrderedLayout layout;
  private final ComponentContainer componentContainer;
  private final ClickListener tabChangeListener = new TabChangeListener();
  private final Map<Button, Component> buttonComponentMap = new LinkedHashMap<Button, Component>();
  private final List<Button> tabs = new ArrayList<Button>();
  private Button shownTab = null;
  private final Orientation orientation;

  private static final String CLASS = "detachedtabs";
  private static final String CLASS_HORIZONTAL = CLASS + "-horizontal";
  private static final String CLASS_VERTICAL = CLASS + "-vertical";

  private static final String TAB_CLASS = "tab";
  private static final String TAB_FIRST_CLASS = TAB_CLASS + "-first";
  private static final String TAB_LAST_CLASS = TAB_CLASS + "-last";
  private static final String TAB_SELECTED_CLASS = TAB_CLASS + "-selected";

  private DetachedTabs(final ComponentContainer componentContainer,
      final Orientation orientation) {

    if (componentContainer == null || orientation == null) {
      throw new NullPointerException("arguments may not be null");
    }

    setStyleName(CLASS);

    setSizeFull();

    switch (orientation) {
    case HORIZONTAL:
      layout = new HorizontalLayout();
      addStyleName(CLASS_HORIZONTAL);
      setHeight("30px");
      break;
    case VERTICAL:
      layout = new VerticalLayout();
      addStyleName(CLASS_VERTICAL);
      setWidth("100px");
      break;
    default:
      throw new UnsupportedOperationException("orientation " + orientation
          + " not supported");
    }

    layout.setSizeFull();

    setCompositionRoot(layout);
    this.componentContainer = componentContainer;
    this.orientation = orientation;
  }

  /**
   * Add a tab
   * 
   * @param content
   *          The {@link Component} that will be shown once its corresponding
   *          tab is selected.
   * @param caption
   *          The caption for the tab.
   */
  public void addTab(final Component content, final String caption) {
    if (content == null || caption == null) {
      throw new NullPointerException("Arguments may not be null");
    }

    final Button button = new NativeButton(caption, tabChangeListener);

    if (orientation == Orientation.HORIZONTAL) {
      button.setHeight("100%");
      button.setWidth(getWidth(), getWidthUnits());
    } else {
      button.setHeight(getHeight(), getHeightUnits());
      button.setWidth("100%");
    }

    layout.addComponent(button);
    buttonComponentMap.put(button, content);
    tabs.add(button);

    adjustTabStyles();

    if (shownTab == null) {
      switchTo(content, button);
    }
  }

  private void adjustTabStyles() {
    if (!tabs.isEmpty()) {
      final Component first = tabs.get(0);
      Component last = first;

      for (final Component tab : tabs) {
        tab.setStyleName(TAB_CLASS);
        last = tab;
      }

      first.addStyleName(TAB_FIRST_CLASS);
      last.addStyleName(TAB_LAST_CLASS);

      if (shownTab != null) {
        shownTab.addStyleName(TAB_SELECTED_CLASS);
      }
    }
  }

  private void switchTo(final Component componentToSwitch, final Button button) {
    componentContainer.removeAllComponents();
    componentContainer.addComponent(componentToSwitch);
    shownTab = button;

    adjustTabStyles();
  }

  @Override
  public void setWidth(final float width, final int unit) {
    if (orientation == Orientation.VERTICAL) {
      final Iterator<Component> i = layout.getComponentIterator();
      while (i.hasNext()) {
        i.next().setWidth(width, unit);
      }
    }

    super.setWidth(width, unit);
  }

  @Override
  public void setHeight(final float height, final int unit) {
    if (orientation == Orientation.HORIZONTAL) {
      final Iterator<Component> i = layout.getComponentIterator();
      while (i.hasNext()) {
        i.next().setHeight(height, unit);
      }
    }

    super.setHeight(height, unit);
  }
}

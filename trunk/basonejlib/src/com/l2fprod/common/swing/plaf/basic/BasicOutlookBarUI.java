/**
 * L2FProd.com Common Components 7.3 License.
 *
 * Copyright 2005-2007 L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.l2fprod.common.swing.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.l2fprod.common.swing.JOutlookBar;
import com.l2fprod.common.swing.PercentLayout;
import com.l2fprod.common.swing.PercentLayoutAnimator;
import com.l2fprod.common.swing.plaf.OutlookBarUI;
import com.l2fprod.common.util.JVM;

/**
 * BasicOutlookBarUI. <br>
 * 
 */
@SuppressWarnings("unchecked")
public class BasicOutlookBarUI extends BasicTabbedPaneUI implements
		OutlookBarUI {

	private static final String BUTTON_ORIGINAL_FOREGROUND = "TabButton/foreground";
	private static final String BUTTON_ORIGINAL_BACKGROUND = "TabButton/background";

	public static ComponentUI createUI(final JComponent c) {
		return new BasicOutlookBarUI();
	}

	private ContainerListener tabListener;
	private Map buttonToTab;
	private Map tabToButton;
	private Component nextVisibleComponent;
	private PercentLayoutAnimator animator;

	public JScrollPane makeScrollPane(final Component component) {
		// the component is not scrollable, wraps it in a ScrollableJPanel
		final JScrollPane scroll = new JScrollPane();
		scroll.setBorder(BorderFactory.createEmptyBorder());
		if (component instanceof Scrollable) {
			scroll.getViewport().setView(component);
		} else {
			scroll.getViewport().setView(new ScrollableJPanel(component));
		}
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		return scroll;
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		final TabLayout layout = new TabLayout();
		tabPane.setLayout(layout);
		// ensure constraints is correct for existing components
		layout.setLayoutConstraints(tabPane);
		updateTabLayoutOrientation();

		buttonToTab = new HashMap();
		tabToButton = new HashMap();

		LookAndFeel.installBorder(tabPane, "OutlookBar.border");
		LookAndFeel.installColors(tabPane, "OutlookBar.background",
				"OutlookBar.foreground");

		tabPane.setOpaque(true);

		// add buttons for the current components already added in this panel
		final Component[] components = tabPane.getComponents();
		for (int i = 0, c = components.length; i < c; i++) {
			tabAdded(components[i]);
		}
	}

	@Override
	protected void uninstallDefaults() {
		// remove all buttons created for components
		final List tabs = new ArrayList(buttonToTab.values());
		for (final Iterator iter = tabs.iterator(); iter.hasNext();) {
			final Component tab = (Component) iter.next();
			tabRemoved(tab);
		}
		super.uninstallDefaults();
	}

	@Override
	protected void installListeners() {
		tabPane.addContainerListener(tabListener = createTabListener());
		tabPane
				.addPropertyChangeListener(propertyChangeListener = createPropertyChangeListener());
		tabPane.addChangeListener(tabChangeListener = createChangeListener());
	}

	protected ContainerListener createTabListener() {
		return new ContainerTabHandler();
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return new PropertyChangeHandler();
	}

	@Override
	protected ChangeListener createChangeListener() {
		return new ChangeHandler();
	}

	@Override
	protected void uninstallListeners() {
		tabPane.removeChangeListener(tabChangeListener);
		tabPane.removePropertyChangeListener(propertyChangeListener);
		tabPane.removeContainerListener(tabListener);
	}

	@Override
	public Rectangle getTabBounds(final JTabbedPane pane, final int index) {
		final Component tab = pane.getComponentAt(index);
		return tab.getBounds();
	}

	@Override
	public int getTabRunCount(final JTabbedPane pane) {
		return 0;
	}

	@Override
	public int tabForCoordinate(final JTabbedPane pane, final int x, final int y) {
		int index = -1;
		for (int i = 0, c = pane.getTabCount(); i < c; i++) {
			if (pane.getComponentAt(i).contains(x, y)) {
				index = i;
				break;
			}
		}
		return index;
	}

	protected int indexOfComponent(final Component component) {
		int index = -1;
		final Component[] components = tabPane.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] == component) {
				index = i;
				break;
			}
		}
		return index;
	}

	protected TabButton createTabButton() {
		final TabButton button = new TabButton();
		button.setOpaque(true);
		return button;
	}

	protected void tabAdded(final Component newTab) {
		TabButton button = (TabButton) tabToButton.get(newTab);
		if (button == null) {
			button = createTabButton();

			// save the button original color,
			// later they would be restored if the button colors are not
			// customized on
			// the OutlookBar
			button.putClientProperty(BUTTON_ORIGINAL_FOREGROUND, button
					.getForeground());
			button.putClientProperty(BUTTON_ORIGINAL_BACKGROUND, button
					.getBackground());

			buttonToTab.put(button, newTab);
			tabToButton.put(newTab, button);
			button.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent e) {
					final Component current = getVisibleComponent();
					final Component target = newTab;

					// animate the tabPane if there is a current tab selected
					// and if the
					// tabPane allows animation
					if (((JOutlookBar) tabPane).isAnimated()
							&& current != target && current != null
							&& target != null) {
						if (animator != null) {
							animator.stop();
						}
						animator = new PercentLayoutAnimator(tabPane,
								(PercentLayout) tabPane.getLayout()) {

							@Override
							protected void complete() {
								super.complete();
								tabPane.setSelectedComponent(newTab);
								nextVisibleComponent = null;
								// ensure the no longer visible component has
								// its constraint
								// reset to 100% in the case it becomes visible
								// again without
								// animation
								if (current.getParent() == tabPane) {
									((PercentLayout) tabPane.getLayout())
											.setConstraint(current, "100%");
								}
							}
						};
						nextVisibleComponent = newTab;
						animator.setTargetPercent(current, 1.0f, 0.0f);
						animator.setTargetPercent(newTab, 0.0f, 1.0f);
						animator.start();
					} else {
						nextVisibleComponent = null;
						tabPane.setSelectedComponent(newTab);
					}
				}
			});
		} else {
			// the tab is already in the list, remove the button, it will be
			// added again later
			tabPane.remove(button);
		}

		// update the button with the tab information
		updateTabButtonAt(tabPane.indexOfComponent(newTab));

		final int index = indexOfComponent(newTab);
		tabPane.add(button, index);

		// workaround for nullpointerexception in setRolloverTab
		// introduced by J2SE 5
		if (JVM.current().isOneDotFive()) {
			assureRectsCreated(tabPane.getTabCount());
		}
	}

	protected void tabRemoved(final Component removedTab) {
		final TabButton button = (TabButton) tabToButton.get(removedTab);
		tabPane.remove(button);
		buttonToTab.remove(button);
		tabToButton.remove(removedTab);
	}

	/**
	 * Called whenever a property of a tab is changed
	 * 
	 * @param index
	 */
	protected void updateTabButtonAt(final int index) {
		final TabButton button = buttonForTab(index);
		button.setText(tabPane.getTitleAt(index));
		button.setIcon(tabPane.getIconAt(index));
		button.setDisabledIcon(tabPane.getDisabledIconAt(index));

		Color background = tabPane.getBackgroundAt(index);
		if (background == null) {
			background = (Color) button
					.getClientProperty(BUTTON_ORIGINAL_BACKGROUND);
		}
		button.setBackground(background);

		Color foreground = tabPane.getForegroundAt(index);
		if (foreground == null) {
			foreground = (Color) button
					.getClientProperty(BUTTON_ORIGINAL_FOREGROUND);
		}
		button.setForeground(foreground);

		button.setToolTipText(tabPane.getToolTipTextAt(index));
		button.setDisplayedMnemonicIndex(tabPane
				.getDisplayedMnemonicIndexAt(index));
		button.setMnemonic(tabPane.getMnemonicAt(index));
		button.setEnabled(tabPane.isEnabledAt(index));
		button.setHorizontalAlignment(((JOutlookBar) tabPane)
				.getAlignmentAt(index));
	}

	protected TabButton buttonForTab(final int index) {
		final Component component = tabPane.getComponentAt(index);
		return (TabButton) tabToButton.get(component);
	}

	class ChangeHandler implements ChangeListener {

		public void stateChanged(final ChangeEvent e) {
			final JTabbedPane tabPane = (JTabbedPane) e.getSource();
			tabPane.revalidate();
			tabPane.repaint();
		}
	}

	class PropertyChangeHandler implements PropertyChangeListener {

		public void propertyChange(final PropertyChangeEvent e) {
			// JTabbedPane pane = (JTabbedPane)e.getSource();
			final String name = e.getPropertyName();
			if ("tabPropertyChangedAtIndex".equals(name)) {
				final int index = ((Integer) e.getNewValue()).intValue();
				updateTabButtonAt(index);
			} else if ("tabPlacement".equals(name)) {
				updateTabLayoutOrientation();
			}
		}
	}

	protected void updateTabLayoutOrientation() {
		final TabLayout layout = (TabLayout) tabPane.getLayout();
		final int placement = tabPane.getTabPlacement();
		if (placement == JTabbedPane.TOP || placement == JTabbedPane.BOTTOM) {
			layout.setOrientation(PercentLayout.HORIZONTAL);
		} else {
			layout.setOrientation(PercentLayout.VERTICAL);
		}
	}

	/**
	 * Manages tabs being added or removed
	 */
	class ContainerTabHandler extends ContainerAdapter {

		@Override
		public void componentAdded(final ContainerEvent e) {
			if (!(e.getChild() instanceof UIResource)) {
				final Component newTab = e.getChild();
				tabAdded(newTab);
			}
		}

		@Override
		public void componentRemoved(final ContainerEvent e) {
			if (!(e.getChild() instanceof UIResource)) {
				final Component oldTab = e.getChild();
				tabRemoved(oldTab);
			}
		}
	}

	/**
	 * Layout for the tabs, buttons get preferred size, tabs get all
	 */
	protected class TabLayout extends PercentLayout {

		@Override
		public void addLayoutComponent(final Component component,
				final Object constraints) {
			if (constraints == null) {
				if (component instanceof TabButton) {
					super.addLayoutComponent(component, "");
				} else {
					super.addLayoutComponent(component, "100%");
				}
			} else {
				super.addLayoutComponent(component, constraints);
			}
		}

		public void setLayoutConstraints(final Container parent) {
			final Component[] components = parent.getComponents();
			for (int i = 0, c = components.length; i < c; i++) {
				if (!(components[i] instanceof TabButton)) {
					super.addLayoutComponent(components[i], "100%");
				}
			}
		}

		@Override
		public void layoutContainer(final Container parent) {
			final int selectedIndex = tabPane.getSelectedIndex();
			final Component visibleComponent = getVisibleComponent();

			if (selectedIndex < 0) {
				if (visibleComponent != null) {
					// The last tab was removed, so remove the component
					setVisibleComponent(null);
				}
			} else {
				final Component selectedComponent = tabPane
						.getComponentAt(selectedIndex);
				boolean shouldChangeFocus = false;

				// In order to allow programs to use a single component
				// as the display for multiple tabs, we will not change
				// the visible compnent if the currently selected tab
				// has a null component. This is a bit dicey, as we don't
				// explicitly state we support this in the spec, but since
				// programs are now depending on this, we're making it work.
				//
				if (selectedComponent != null) {
					if (selectedComponent != visibleComponent
							&& visibleComponent != null) {
						// get the current focus owner
						final Component currentFocusOwner = KeyboardFocusManager
								.getCurrentKeyboardFocusManager()
								.getFocusOwner();
						// check if currentFocusOwner is a descendant of
						// visibleComponent
						if (currentFocusOwner != null
								&& SwingUtilities.isDescendingFrom(
										currentFocusOwner, visibleComponent)) {
							shouldChangeFocus = true;
						}
					}
					setVisibleComponent(selectedComponent);

					// make sure only the selected component is visible
					final Component[] components = parent.getComponents();
					for (int i = 0; i < components.length; i++) {
						if (!(components[i] instanceof UIResource)
								&& components[i].isVisible()
								&& components[i] != selectedComponent) {
							components[i].setVisible(false);
							setConstraint(components[i], "*");
						}
					}

					if (BasicOutlookBarUI.this.nextVisibleComponent != null) {
						BasicOutlookBarUI.this.nextVisibleComponent
								.setVisible(true);
					}
				}

				super.layoutContainer(parent);

				if (shouldChangeFocus) {
					if (!requestFocusForVisibleComponent0()) {
						tabPane.requestFocus();
					}
				}
			}
		}
	}

	// PENDING(fred) JDK 1.5 may have this method from superclass
	@SuppressWarnings("deprecation")
	protected boolean requestFocusForVisibleComponent0() {
		final Component visibleComponent = getVisibleComponent();
		if (visibleComponent.isFocusable()) {
			visibleComponent.requestFocus();
			return true;
		} else if (visibleComponent instanceof JComponent) {
			if (((JComponent) visibleComponent).requestDefaultFocus()) { return true; }
		}
		return false;
	}

	protected static class TabButton extends JButton implements UIResource {

		public TabButton() {}

		public TabButton(final ButtonUI ui) {
			setUI(ui);
		}
	}

	//
	//
	//

	/**
	 * Overriden to return an empty adapter, the default listener was just
	 * implementing the tab selection mechanism
	 */
	@Override
	protected MouseListener createMouseListener() {
		return new MouseAdapter() {};
	}

	/**
	 * Wraps any component in a Scrollable JPanel so it can work correctly
	 * within a viewport
	 */
	private static class ScrollableJPanel extends JPanel implements Scrollable {

		public ScrollableJPanel(final Component component) {
			setLayout(new BorderLayout(0, 0));
			add("Center", component);
			setOpaque(false);
		}

		public int getScrollableUnitIncrement(final Rectangle visibleRect,
				final int orientation, final int direction) {
			return 16;
		}

		public Dimension getPreferredScrollableViewportSize() {
			return (super.getPreferredSize());
		}

		public int getScrollableBlockIncrement(final Rectangle visibleRect,
				final int orientation, final int direction) {
			return 16;
		}

		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}

	//
	// Override all paint methods of the BasicTabbedPaneUI to do nothing
	//

	@Override
	public void paint(final Graphics g, final JComponent c) {}

	@Override
	protected void paintContentBorder(final Graphics g, final int tabPlacement,
			final int selectedIndex) {}

	@Override
	protected void paintContentBorderBottomEdge(final Graphics g,
			final int tabPlacement, final int selectedIndex, final int x,
			final int y, final int w, final int h) {}

	@Override
	protected void paintContentBorderLeftEdge(final Graphics g,
			final int tabPlacement, final int selectedIndex, final int x,
			final int y, final int w, final int h) {}

	@Override
	protected void paintContentBorderRightEdge(final Graphics g,
			final int tabPlacement, final int selectedIndex, final int x,
			final int y, final int w, final int h) {}

	@Override
	protected void paintContentBorderTopEdge(final Graphics g,
			final int tabPlacement, final int selectedIndex, final int x,
			final int y, final int w, final int h) {}

	@Override
	protected void paintFocusIndicator(final Graphics g,
			final int tabPlacement, final Rectangle[] rects,
			final int tabIndex, final Rectangle iconRect,
			final Rectangle textRect, final boolean isSelected) {}

	@Override
	protected void paintIcon(final Graphics g, final int tabPlacement,
			final int tabIndex, final Icon icon, final Rectangle iconRect,
			final boolean isSelected) {}

	@Override
	protected void paintTab(final Graphics g, final int tabPlacement,
			final Rectangle[] rects, final int tabIndex,
			final Rectangle iconRect, final Rectangle textRect) {}

	@Override
	protected void paintTabArea(final Graphics g, final int tabPlacement,
			final int selectedIndex) {}

	@Override
	protected void paintTabBackground(final Graphics g, final int tabPlacement,
			final int tabIndex, final int x, final int y, final int w,
			final int h, final boolean isSelected) {}

	@Override
	protected void paintTabBorder(final Graphics g, final int tabPlacement,
			final int tabIndex, final int x, final int y, final int w,
			final int h, final boolean isSelected) {}

	@Override
	protected void paintText(final Graphics g, final int tabPlacement,
			final Font font, final FontMetrics metrics, final int tabIndex,
			final String title, final Rectangle textRect,
			final boolean isSelected) {}
}
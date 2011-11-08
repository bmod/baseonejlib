/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicobject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.swt;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;

/**
 * A view helper that displays an {@link EventList} in a {@link List}.
 *
 * <p>This class is not thread safe. It must be used exclusively with the SWT
 * event handler thread.
 *
 * @author <a href="mailto:kevin@swank.ca">Kevin Maltby</a>
 */
public class EventListViewer<E> implements ListEventListener<E> {

    /** the SWT List */
    private List list = null;

    /** the proxy moves events to the SWT user interface thread */
    private TransformedList<E, E> swtThreadSource = null;

    /** the formatter for list elements */
    private ILabelProvider labelProvider = null;

    /** For selection management */
    private SelectionManager<E> selection = null;

    /**
     * Creates a new List that displays and responds to changes in source.
     * List elements will simply be displayed as the result of calling
     * toString() on the contents of the source list.
     */
    public EventListViewer(EventList<E> source, List list) {
        this(source, list, new LabelProvider());
    }

    /**
     * Creates a new List that displays and responds to changes in source.
     * List elements are formatted using the provided {@link ILabelProvider}.
     */
    public EventListViewer(EventList<E> source, List list, ILabelProvider labelProvider) {
        // lock the source list for reading since we want to prevent writes
        // from occurring until we fully initialize this EventListViewer
        source.getReadWriteLock().readLock().lock();
        try {
            swtThreadSource = GlazedListsSWT.swtThreadProxyList(source, list.getDisplay());
            this.list = list;
            this.labelProvider = labelProvider;

            // Enable the selection lists
            selection = new SelectionManager<E>(swtThreadSource, new SelectableList());

            // setup initial values
            for(int i = 0, n = swtThreadSource.size(); i < n; i++) {
                addRow(i, swtThreadSource.get(i));
            }

            // listen for changes
            swtThreadSource.addListEventListener(this);
        } finally {
            source.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Gets the List's {@link ILabelProvider}.
     */
    public ILabelProvider getLabelProvider() {
        return labelProvider;
    }

    /**
     * Gets the List being managed by this {@link EventListViewer}.
     */
    public List getList() {
        return list;
    }

    /**
     * Provides access to an {@link EventList} that contains items from the
     * viewed Table that are not currently selected.
     */
    public EventList<E> getDeselected() {
        swtThreadSource.getReadWriteLock().readLock().lock();
        try {
            return selection.getSelectionList().getDeselected();
        } finally {
            swtThreadSource.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Gets an {@link EventList} that contains only deselected values and
     * modifies the selection state on mutation.
     *
     * Adding an item to this list deselects it and removing an item selects it.
     * If an item not in the source list is added an
     * {@link IllegalArgumentException} is thrown
     */
    public EventList<E> getTogglingDeselected() {
        swtThreadSource.getReadWriteLock().readLock().lock();
        try {
            return selection.getSelectionList().getTogglingDeselected();
        } finally {
            swtThreadSource.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Provides access to an {@link EventList} that contains items from the
     * viewed Table that are currently selected.
     */
    public EventList<E> getSelected() {
        swtThreadSource.getReadWriteLock().readLock().lock();
        try {
            return selection.getSelectionList().getSelected();
        } finally {
            swtThreadSource.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Gets an {@link EventList} that contains only selected
     * values and modifies the selection state on mutation.
     *
     * Adding an item to this list selects it and removing an item deselects it.
     * If an item not in the source list is added an
     * {@link IllegalArgumentException} is thrown.
     */
    public EventList<E> getTogglingSelected() {
        swtThreadSource.getReadWriteLock().readLock().lock();
        try {
            return selection.getSelectionList().getTogglingSelected();
        } finally {
            swtThreadSource.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Adds the value at the specified row.
     */
    private void addRow(int row, Object value) {
        list.add(labelProvider.getText(value), row);
    }

    /**
     * Updates the value at the specified row.
     */
    private void updateRow(int row, Object value) {
        list.setItem(row, labelProvider.getText(value));
    }

    /**
     * Removes the value at the specified row.
     */
    private void deleteRow(int row) {
        list.remove(row);
    }

    /**
     * When the source list is changed, this forwards the change to the
     * displayed List.
     */
    public void listChanged(ListEvent<E> listChanges) {
        int firstModified = swtThreadSource.size();
        // Apply the list changes
        while (listChanges.next()) {
            int changeIndex = listChanges.getIndex();
            int changeType = listChanges.getType();

            if (changeType == ListEvent.INSERT) {
                addRow(changeIndex, swtThreadSource.get(changeIndex));
                firstModified = Math.min(changeIndex, firstModified);
            } else if (changeType == ListEvent.UPDATE) {
                updateRow(changeIndex, swtThreadSource.get(changeIndex));
            } else if (changeType == ListEvent.DELETE) {
                deleteRow(changeIndex);
                firstModified = Math.min(changeIndex, firstModified);
            }
        }

        // Reapply selection to the List
        selection.fireSelectionChanged(firstModified, swtThreadSource.size() - 1);
    }

    /**
     * Inverts the current selection.
     */
    public void invertSelection() {
        selection.getSelectionList().invertSelection();
    }


    /**
     * To use common selectable widget logic in a widget unaware fashion.
     */
    private final class SelectableList implements Selectable {
        /** {@inheritDoc} */
        public void addSelectionListener(SelectionListener listener) {
            list.addSelectionListener(listener);
        }

        /** {@inheritDoc} */
        public void removeSelectionListener(SelectionListener listener) {
            list.removeSelectionListener(listener);
        }

        /** {@inheritDoc} */
        public int getSelectionIndex() {
            return list.getSelectionIndex();
        }

        /** {@inheritDoc} */
        public int[] getSelectionIndices() {
            return list.getSelectionIndices();
        }

        /** {@inheritDoc} */
        public int getStyle() {
            return list.getStyle();
        }

        /** {@inheritDoc} */
        public void select(int index) {
            list.select(index);
        }

        /** {@inheritDoc} */
        public void deselect(int index) {
            list.deselect(index);
        }
    }

    /**
     * Releases the resources consumed by this {@link EventListViewer} so that it
     * may eventually be garbage collected.
     *
     * <p>An {@link EventListViewer} will be garbage collected without a call to
     * {@link #dispose()}, but not before its source {@link EventList} is garbage
     * collected. By calling {@link #dispose()}, you allow the {@link EventListViewer}
     * to be garbage collected before its source {@link EventList}. This is
     * necessary for situations where an {@link EventListViewer} is short-lived but
     * its source {@link EventList} is long-lived.
     *
     * <p><strong><font color="#FF0000">Warning:</font></strong> It is an error
     * to call any method on a {@link EventListViewer} after it has been disposed.
     */
    public void dispose() {
        selection.dispose();

        swtThreadSource.removeListEventListener(this);
        swtThreadSource.dispose();
    }
}
/* Glazed Lists                                                 (c) 2003-2006 */
/* http://publicobject.com/glazedlists/                      publicboject.com,*/
/*                                                     O'Dell Engineering Ltd.*/
package ca.odell.glazedlists.swt;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Combo;

/**
 * A view helper that displays an {@link EventList} in a {@link Combo} component.
 *
 * <p>This class is not thread safe. It must be used exclusively with the SWT
 * event handler thread.
 *
 * @author <a href="mailto:kevin@swank.ca">Kevin Maltby</a>
 */
public class EventComboViewer<E> implements ListEventListener<E> {

    /** the SWT Combo component */
    private Combo combo = null;

    /** the EventList to respond to */
    private TransformedList<E, E> swtSource = null;

    /** the label provider to pretty print a String representation of each Object */
    private ILabelProvider labelProvider = null;

    /**
     * Binds the contents of a {@link Combo} component to an {@link EventList}
     * source.  This allows the selection choices in a {@link Combo} to change
     * dynamically to reflect chances to the source {@link EventList}.  The
     * {@link String} values displayed in the {@link Combo} compoment will be
     * the result of calling toString() on the Objects found in source.
     */
    public EventComboViewer(EventList<E> source, Combo combo) {
        this(source, combo, new LabelProvider());
    }

    /**
     * Binds the contents of a {@link Combo} component to an {@link EventList}
     * source.  This allows the selection choices in a {@link Combo} to change
     * dynamically to reflect chances to the source {@link EventList}.  The
     * {@link String} values displayed in the {@link Combo} compoment will be
     * formatted using the provided {@link ILabelProvider}.
     *
     * @see ILabelProvider
     * @see GlazedListsSWT#beanLabelProvider(String)
     */
    public EventComboViewer(EventList<E> source, Combo combo, ILabelProvider labelProvider) {
        // lock the source list for reading since we want to prevent writes
        // from occurring until we fully initialize this EventComboViewer
        source.getReadWriteLock().readLock().lock();
        try {
            this.swtSource = GlazedListsSWT.swtThreadProxyList(source, combo.getDisplay());
            this.combo = combo;
            this.labelProvider = labelProvider;

            // set the initial data
            for(int i = 0, n = source.size(); i < n; i++) {
                addRow(i, source.get(i));
            }

            // listen for changes
            swtSource.addListEventListener(this);
        } finally {
            source.getReadWriteLock().readLock().unlock();
        }
    }

    /**
     * Gets the Combo being managed by this {@link EventComboViewer}.
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * Adds the value at the specified row.
     */
    private void addRow(int row, Object value) {
        combo.add(labelProvider.getText(value), row);
    }

    /**
     * Updates the value at the specified row.
     */
    private void updateRow(int row, Object value) {
        combo.setItem(row, labelProvider.getText(value));
    }

    /**
     * Removes the value at the specified row.
     */
    private void deleteRow(int row) {
        combo.remove(row);
    }

    /**
     * When the source combo is changed, this forwards the change to the
     * displayed combo.
     */
    public void listChanged(ListEvent listChanges) {
        // apply the combo changes
        while (listChanges.next()) {
            int changeIndex = listChanges.getIndex();
            int changeType = listChanges.getType();

            if (changeType == ListEvent.INSERT)
                addRow(changeIndex, swtSource.get(changeIndex));
            else if (changeType == ListEvent.UPDATE)
                updateRow(changeIndex, swtSource.get(changeIndex));
            else if (changeType == ListEvent.DELETE)
                deleteRow(changeIndex);
        }
    }

    /**
     * Releases the resources consumed by this {@link EventComboViewer} so that it
     * may eventually be garbage collected.
     *
     * <p>An {@link EventComboViewer} will be garbage collected without a call to
     * {@link #dispose()}, but not before its source {@link EventList} is garbage
     * collected. By calling {@link #dispose()}, you allow the {@link EventComboViewer}
     * to be garbage collected before its source {@link EventList}. This is
     * necessary for situations where an {@link EventComboViewer} is short-lived but
     * its source {@link EventList} is long-lived.
     *
     * <p><strong><font color="#FF0000">Warning:</font></strong> It is an error
     * to call any method on a {@link EventComboViewer} after it has been disposed.
     */
    public void dispose() {
        swtSource.removeListEventListener(this);
        swtSource.dispose();
    }
}
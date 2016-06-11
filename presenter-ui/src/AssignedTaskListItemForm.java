import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Admin on 6/11/2016.
 */
public class AssignedTaskListItemForm extends DefaultListCellRenderer {
    private JPanel pnlContainer;
    private JTextArea txtDescription;

    AssignedTaskListItemForm()
    {

    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value,  int index, boolean selected, boolean expanded)
    {
        ComplexListItem item = (ComplexListItem)value;
        TitledBorder tb = (TitledBorder) this.pnlContainer.getBorder();
        tb.setTitle(item.getTitle());
        txtDescription.setText(item.getDescription());
        txtDescription.setOpaque(false);
        txtDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        pnlContainer.setOpaque(false);
        return pnlContainer;
    }


    public static class ComplexListItem
    {
        private String title;
        private String description;
        public ComplexListItem(String title, String description)
        {
            this.title = title;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public String getTitle() {
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public void setTitle(String title) {
            this.title = title;
        }
    }

}

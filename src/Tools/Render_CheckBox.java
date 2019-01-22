package Tools;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class Render_CheckBox extends JCheckBox implements TableCellRenderer{
    private JComponent component = new JCheckBox();
    //constructor de clase
    public Render_CheckBox(){
        setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column){
    //color de fondo de la celda 
        ((JCheckBox)component).setBackground(new Color(100,200,0));
        //obtiene valor boolean y coloca valor en el checkbox
        boolean b = ((Boolean)value);
        ((JCheckBox)component).setSelected(b);
        
        if(((JCheckBox)component).isSelected()){
            ((JCheckBox)component).setText("Oui");
        }
        else{
            ((JCheckBox)component).setText("Non");
        }
        return ((JCheckBox)component);
    }
}

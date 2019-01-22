package Tools;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class Celda_CheckBox extends DefaultCellEditor implements TableCellRenderer{
    private JComponent component = new JCheckBox();
    private boolean value = false;  // valor de la celda
    //constructor de la clase
    public Celda_CheckBox(){
        super(new JCheckBox());
    }
    @Override  //retorna el valor de la celda
    public Object getCellEditorValue(){
        return ((JCheckBox)component).isSelected();
    }
   // segun el valor seleccionada/deseleccionada el jcheckbox
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
        //color de fondo en modo edicion
        ((JCheckBox)component).setBackground(new Color(200,100,0));
        //obtiene el valor de celda y coloca en el jCheckBox
        boolean b = ((Boolean)value).booleanValue();
        ((JCheckBox)component).setSelected(b);
        return ((JCheckBox)component);
    }
    //cuando termina la manipulacion de celda
    public boolean stopCelleEditing(){
         value=((Boolean)getCellEditorValue()).booleanValue();
         ((JCheckBox)component).setSelected(value);
         return super.stopCellEditing();
     }
    //retorna componente
    @Override
     public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column){
         if(value==null){
             return null;
         }
         return ((JCheckBox)component);
     }

}

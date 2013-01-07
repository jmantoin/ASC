/**
 * @author Adam
 */
package GUI;

import javax.swing.table.AbstractTableModel;

public class HexModel extends AbstractTableModel
{

    private String[] columnNames = {""};
    private Object[][] data = new String[256][1];

    /**
     *  Constructor 1: Calls setData. 
     */
    public HexModel()
    {
        setData();
    }

    /**
     *  Constructor 2: Calls setData2 with the value of a.
     */
    public HexModel(int a)
    {
        setData2(a);
    }

    /**
     *  Constructor 3: Calls setData3 with the value of max.
     */
    public HexModel(boolean a, int max)
    {
        data = new String[max][1];
        setData3(max);
    }

    /**
     *  Creates a new object array with the new size and sets all values to "".
     */
    public void setData3(int max)
    {
        Object[][] newData = new String[max][1];

        for (int i = 0; i < max; i++) {
            newData[i][0] = "";
        }

        data = newData;
    }

    /**
     *  Creates a new object array with the new size and sets the values from 0
     *  to max in hex.
     */
    public void setData2(int a)
    {
        Object[][] newData = new String[a][1];
        for (int i = 0; i < a; i++) {
            String num = Integer.toHexString(i);

            if (num.length() < 4) {
                while (num.length() < 4) {
                    num = "0" + num;
                }
            }

            num = num.toUpperCase();
            newData[i][0] = num;
        }
        data = newData;
    }

    /**
     *  The default method called when the program is started.
     */
    public void setData()
    {
        for (int i = 0; i < 256; i++) {
            data[i][0] = "";
        }
    }

    /**
     *  Remaining methods are just overriden methods with no changes.
     */
    
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return data.length;
    }

    @Override
    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column would
     * contain text ("true"/"false"), rather than a check box.
     */
    @Override
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col)
    {

        data[row][col] = value;
        fireTableCellUpdated(row, col);

    }

    private void printDebugData()
    {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                System.out.print("  " + data[i][j]);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
}
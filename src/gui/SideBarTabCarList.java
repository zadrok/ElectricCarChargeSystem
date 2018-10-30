package gui;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import model.Car;

@SuppressWarnings("serial")
public class SideBarTabCarList extends SideBarTab
{
	private JScrollPane scrollPane;
	public JTree content;
	
	public SideBarTabCarList(SideBar aSideBar)
	{
		super(aSideBar);
		
		fillContent();
		initScrollPane();
		initTreeSelectionListener();
	}
	
	public void updateSize(int x, int y, int width, int height)
	{
		scrollPane.setBounds(0, 0, width-6, height-100);
	}
	
	private void fillContent()
	{	
		DefaultMutableTreeNode cars = new DefaultMutableTreeNode("Cars");
		
		for ( Car lCar : getChargerSystem().getCarAgents() )
			cars.add( new DefaultMutableTreeNode( lCar ) );

		content = new JTree(cars);
	}
	
	private void initScrollPane()
	{
		scrollPane = new JScrollPane(content);
		scrollPane.setBounds(0, 0, getWidth()-6, getHeight()-100);
		
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(scrollPane);
	}
	
	private void initTreeSelectionListener()
	{
		content.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		content.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
				DefaultMutableTreeNode selectedObject = (DefaultMutableTreeNode) content.getLastSelectedPathComponent();
				
				if (selectedObject != null)
				{
					try 
					{
						getGUI().setSelectedCar( (Car) selectedObject.getUserObject() );
					}
					catch (Exception ex)
					{
						//System.out.println("Error in object list");
					}
				}
				
			}
		});
	}
	
	public void refresh()
	{
		remove(scrollPane);
		content = new JTree();
		scrollPane = new JScrollPane();
		
		fillContent();
		initScrollPane();
		initTreeSelectionListener();
		
		validate();
		repaint();
	}

}

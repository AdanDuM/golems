/* Generated By:JJTree: Do not edit this line. ASTDefinitionClass.java */

package support.translate.parser;

@SuppressWarnings("all")
public class ASTDefinitionClass extends SimpleNode {
	private String name;
	private int endLine; 
	public int getEndLine() {
		return endLine;
	}
	public void setLine(int line)
	{
		this.endLine = line;
	}
	public String getName() {
		if(name == null || name.length()<2)
			return "";
		return name.substring(0, name.length()-1).trim();
	}
	public void setName(Token t)
	{
		name = t.image.intern();
	}
  public ASTDefinitionClass(int id) {
    super(id);
  }

  public ASTDefinitionClass(Translator p, int id) {
    super(p, id);
  }
@Override
public NodeType getNodeType() {
	return NodeType.DefinitionClass;
}
@Override
public boolean equals(Object obj) {
	if (obj instanceof ASTDefinitionClass)
	{
		if(((ASTDefinitionClass) obj).name.equalsIgnoreCase(name))
		{
			return true;
		}
	}
	return false;
}
}

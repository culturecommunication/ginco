package fr.mcc.ginco.extjs.view.node;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.utils.LabelUtil;

public class WarningNode extends ThesaurusListBasicNode {

	
	public WarningNode(int maxResult) {
		this.setType(ThesaurusListNodeType.WARNING);
		this.setId(UUID.randomUUID().toString());
		this.setLeaf(true);
		this.setDisplayable(false);
		this.setChildren(new ArrayList<IThesaurusListNode>());
		this.setIconCls("icon-warning");
		this.setTitle(MessageFormat.format(LabelUtil.getResourceLabel("too-many-concepts"), maxResult));
    }
}

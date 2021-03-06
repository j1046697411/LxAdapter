package com.zfy.lxadapter.helper;

import com.zfy.lxadapter.Lx;
import com.zfy.lxadapter.LxAdapter;
import com.zfy.lxadapter.LxList;
import com.zfy.lxadapter.data.LxContext;
import com.zfy.lxadapter.data.LxModel;

import java.util.List;

/**
 * CreateAt : 2019-09-09
 * Describe :
 * <p>
 * 分组列表，展开和收起，删除等
 *
 * @author chendong
 */
public class LxExpandable {


    public static final int GROUP_TYPE = Lx.ViewType.EXPANDABLE_GROUP;
    public static final int CHILD_TYPE = Lx.ViewType.EXPANDABLE_CHILD;

    public interface ExpandableGroup<G, C> {
//        public List<C> children;
//        public boolean expand;
//        public int     groupId;

        List<C> getChildren();

        // void setChildren(List<C> children);

        boolean isExpand();

        void setExpand(boolean expand);

        int getGroupId();

        // void setGroupId(int groupId);
    }

    public interface ExpandableChild<G, C> {

//        public int childId;
//        public int groupId;
//        public G   groupData;

        // int getChildId();

        // void setChildId(int childId);

        int getGroupId();

        // void setGroupId(int groupId);

        G getGroupData();

        // void setGroupData(G groupData);
    }

    public static <G extends ExpandableGroup, C extends ExpandableChild>
    void toggleExpand(LxAdapter adapter, LxContext context, ExpandableGroup<G, C> group) {
        if (group.isExpand()) {
            unExpand(adapter, context, group);
        } else {
            expand(adapter, context, group);
        }
    }


    public static <G extends ExpandableGroup, C extends ExpandableChild>
    void expand(LxAdapter adapter, LxContext context, ExpandableGroup<G, C> group) {
        LxList models = adapter.getData();
        List<LxModel> lxModels = LxSource.just(CHILD_TYPE, group.getChildren()).asModels();
        models.updateAddAll(context.layoutPosition + 1, lxModels);
        models.updateSet(context.model, item -> {
            ExpandableGroup<G, C> groupData = item.unpack();
            groupData.setExpand(true);
        });
    }


    public static <G extends ExpandableGroup, C extends ExpandableChild>
    void unExpand(LxAdapter adapter, LxContext context, ExpandableGroup<G, C> group) {
        LxList models = adapter.getData();
        models.updateRemove(item -> {
            return item.getItemType() == CHILD_TYPE && item.<ExpandableChild<G, C>>unpack().getGroupId() == group.getGroupId();
        });
        models.updateSet(context.model, item -> {
            ExpandableGroup<G, C> groupData = item.unpack();
            groupData.setExpand(false);
        });
    }

    public static <G extends ExpandableGroup, C extends ExpandableChild>
    void removeChild(LxAdapter adapter, LxContext context, ExpandableChild<G, C> data) {
        G groupData = data.getGroupData();
        groupData.getChildren().remove(data);
        LxList models = adapter.getData();
        models.updateRemove(context.layoutPosition);
    }
}

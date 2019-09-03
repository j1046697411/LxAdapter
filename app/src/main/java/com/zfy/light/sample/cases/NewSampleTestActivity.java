package com.zfy.light.sample.cases;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.march.common.pool.ExecutorsPool;
import com.zfy.adapter.Lx;
import com.zfy.adapter.LxAdapter;
import com.zfy.adapter.LxItemBind;
import com.zfy.adapter.LxVh;
import com.zfy.adapter.animation.BindScaleAnimator;
import com.zfy.adapter.component.LxDragSwipeComponent;
import com.zfy.adapter.component.LxEndEdgeLoadMoreComponent;
import com.zfy.adapter.component.LxFixedComponent;
import com.zfy.adapter.component.LxSelectComponent;
import com.zfy.adapter.component.LxSnapComponent;
import com.zfy.adapter.component.LxStartEdgeLoadMoreComponent;
import com.zfy.adapter.data.Copyable;
import com.zfy.adapter.data.Diffable;
import com.zfy.adapter.data.LxContext;
import com.zfy.adapter.data.LxModel;
import com.zfy.adapter.data.TypeOpts;
import com.zfy.adapter.function.LxGlobal;
import com.zfy.adapter.function.LxTransformations;
import com.zfy.adapter.list.LxDiffList;
import com.zfy.adapter.list.LxList;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.R;
import com.zfy.light.sample.Utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CreateAt : 2019-08-31
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.new_sample_activity)
public class NewSampleTestActivity extends MvpActivity {

    public static final int TYPE_STUDENT = Lx.incrementViewType();
    public static final int TYPE_TEACHER = Lx.incrementViewType();

    @BindView(R.id.content_rv)    RecyclerView mRecyclerView;
    @BindView(R.id.fix_container) ViewGroup    mFixContainerFl;

    private LxList<LxModel> mLxModels = new LxDiffList<>();


    private void test() {

        LxList<LxModel> models = new LxDiffList<>();
        LxAdapter.of(models)
                // 指定老师、学生类型，是我们的业务类型，其他的是扩展类型
                .contentType(TYPE_STUDENT, TYPE_TEACHER)
                // 这里指定了 5 种类型的数据绑定
                .binder(new StudentItemBind(), new TeacherItemBind(),
                        new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind())
                .attachTo(mRecyclerView, new GridLayoutManager(getContext(), 3));

        LxDragSwipeComponent.DragSwipeOptions options = new LxDragSwipeComponent.DragSwipeOptions();
        // 关闭触摸自动触发侧滑
        options.touchItemView4Swipe = false;
        // 关闭长按自动触发拖拽
        options.longPressItemView4Drag = false;

        LxAdapter.of(models)
                .binder(new StudentItemBind())
                // 实现 ViewPager 效果
                .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER))
                // 实现 ViewPager 效果，但是可以一次划多个 item
                .component(new LxSnapComponent(Lx.SNAP_MODE_LINEAR))
                .attachTo(mRecyclerView, new LinearLayoutManager(getContext()));

//        List<LxModel> snapshot = models.snapshot();
//        // 添加两个 header
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header1")));
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData("header2")));
//        // 交替添加 10 个学生和老师
//        List<Student> students = ListX.range(10, index -> new Student());
//        List<Teacher> teachers = ListX.range(10, index -> new Teacher());
//        for (int i = 0; i < 10; i++) {
//            snapshot.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
//            snapshot.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
//        }
//        // 添加两个 footer
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer1")));
//        snapshot.add(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("footer2")));
//        // 发布数据更新
//        models.update(snapshot);

    }


    @Override
    public void init() {
        LxGlobal.setImgUrlLoader((view, url, extra) -> {
            Glide.with(view).load(url).into(view);
        });

        LxDragSwipeComponent.DragSwipeOptions dragSwipeOptions = new LxDragSwipeComponent.DragSwipeOptions();
        dragSwipeOptions.longPressItemView4Drag = true;
        dragSwipeOptions.touchItemView4Swipe = true;

        LxAdapter.of(mLxModels)
                .contentType(TYPE_STUDENT, TYPE_TEACHER)
                .binder(new StudentItemBind(), new TeacherItemBind(), new HeaderItemBind(), new FooterItemBind(), new EmptyItemBind())
//                .component(new LxSnapComponent(Lx.SNAP_MODE_PAGER))
                .component(new LxFixedComponent())
//                .component(new LxBindAnimatorComponent())
//                .component(new LxItemAnimatorComponent(new ScaleInLeftAnimator()))
//                .component(new LxSelectComponent(Lx.SELECT_MULTI))
                .component(new LxStartEdgeLoadMoreComponent((component) -> {
                    ToastX.show("顶部加载更多");
                    ExecutorsPool.ui(component::finishLoadMore, 2000);
                }))
                .component(new LxEndEdgeLoadMoreComponent((component) -> { // 加载回调
                    ToastX.show("底部加载更多");
                    ExecutorsPool.ui(component::finishLoadMore, 2000);
                }))
                .component(new LxDragSwipeComponent(dragSwipeOptions, (state, holder, context) -> {
                    switch (state) {
                        case Lx.DRAG_SWIPE_STATE_NONE:
                            break;
                        case Lx.DRAG_STATE_ACTIVE:
                            holder.itemView.animate().scaleX(1.13f).scaleY(1.13f).setDuration(300).start();
                            break;
                        case Lx.DRAG_STATE_RELEASE:
                            holder.itemView.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
                            break;
                        case Lx.SWIPE_STATE_ACTIVE:
                            holder.itemView.setBackgroundColor(Color.GRAY);
                            break;
                        case Lx.SWIPE_STATE_RELEASE:
                            holder.itemView.setBackgroundColor(Color.WHITE);
                            break;
                    }
                }))
                .attachTo(mRecyclerView, new GridLayoutManager(getContext(),3));

        setData();
    }

    @OnClick({R.id.add_header_btn, R.id.add_footer_btn, R.id.empty_btn})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.add_header_btn:
                LxModel header = LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
                mLxModels.updateAdd(1, header);
                break;
            case R.id.add_footer_btn:
                LxModel footer = LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
                mLxModels.updateAdd(mLxModels.size() - 1, footer);
                break;
            case R.id.empty_btn:
                showEmpty();
                break;
            default:
                break;
        }
    }

    private void showEmpty() {
        mLxModels.updateClear();
        mLxModels.updateAdd(LxTransformations.pack(Lx.VIEW_TYPE_EMPTY, new CustomTypeData("", String.valueOf(System.currentTimeMillis()))));
    }


    private void setData() {
        int count = 50;

        List<Student> students = ListX.range(count, index -> new Student(index + " " + System.currentTimeMillis()));

        List<Teacher> teachers = ListX.range(count, index -> new Teacher(index + " " + System.currentTimeMillis()));
        LinkedList<LxModel> lxModels = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            lxModels.add(LxTransformations.pack(TYPE_STUDENT, students.get(i)));
            lxModels.add(LxTransformations.pack(TYPE_TEACHER, teachers.get(i)));
        }
        LxModel header = LxTransformations.pack(Lx.VIEW_TYPE_HEADER, new CustomTypeData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addFirst(header);

        LxModel footer = LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData(Utils.randomImage(), String.valueOf(System.currentTimeMillis())));
        lxModels.addLast(footer);

        mLxModels.update(lxModels);
    }


    // 使用一个自增 ID
    public static int ID = 10;

    static class Student implements Diffable<Student>, Copyable<Student> {

        int    id = ID++;
        String name;

        Student(String name) {
            this.name = name;
        }

        @Override
        public Student copyNewOne() {
            Student student = new Student(name);
            student.id = id;
            return student;
        }

        @Override
        public boolean areContentsTheSame(Student newItem) {
            return name.equals(newItem.name);
        }

        @Override
        public Set<String> getChangePayload(Student newItem) {
            Set<String> strings = new HashSet<>();
            if (!name.equals(newItem.name)) {
                strings.add("name_change");
            }
            return strings;
        }

    }

    static class Teacher {

        String name;

        Teacher(String name) {
            this.name = name;
        }
    }

    static class CustomTypeData {
        String url;
        String desc;

        public CustomTypeData() {
        }

        public CustomTypeData(String desc) {
            this.desc = desc;
        }

        CustomTypeData(String url, String desc) {
            this.url = url;
            this.desc = desc;
        }
    }



    static class StudentItemBind extends LxItemBind<Student> {

        StudentItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire1;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = false;
                opts.viewType = TYPE_STUDENT;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.enableSwipe = true;
                opts.enableFixed = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, Student data, LxModel model, int position, @NonNull List<String> payloads) {
            if (payloads.isEmpty()) {
                holder.setText(R.id.title_tv, "学：" + data.name)
                        .setText(R.id.desc_tv, "支持Swipe，pos = " + position + " ,type =" + TYPE_STUDENT + ", 点击触发payloads更新, 悬停在页面顶部")
                        .swipeOnLongPress(adapter, R.id.title_tv);
            } else {
                for (String payload : payloads) {
                    if (payload.equals("name_change")) {
                        holder.setText(R.id.title_tv, "payloads：" + data.name);
                    }
                }
            }

        }

        @Override
        public void onEvent(LxContext context, Student data, LxModel model, int eventType) {
            ToastX.show("点击学生 position = " + context.position + " data = " + data.name + " eventType = " + eventType);

            LxList<LxModel> list = adapter.getData();
            list.updateSet(context.position, d -> {
                Student unpack = d.unpack();
                unpack.name = String.valueOf(System.currentTimeMillis());
            });
        }
    }

    static class TeacherItemBind extends LxItemBind<Teacher> {

        TeacherItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_squire2;
                opts.viewType = TYPE_TEACHER;
                opts.spanSize = 1;
                opts.enableDrag = true;
                opts.bindAnimator = new BindScaleAnimator();
            }));
        }

        @Override
        public void onBindView(LxVh holder, Teacher data, LxModel model, int position, @NonNull List<String> payloads) {
            holder.setText(R.id.title_tv, "师：" + data.name)
                    .setText(R.id.desc_tv, "支持Drag，pos = " + position + " ,type =" + TYPE_TEACHER)
                    .dragOnLongPress(adapter, R.id.title_tv)
                    .setTextColor(R.id.title_tv, model.isSelected() ? Color.RED : Color.BLACK);
        }

        @Override
        public void onEvent(LxContext context, Teacher data, LxModel model, int eventType) {
            ToastX.show("点击老师 position = " + context.position + " data = " + data.name + " eventType = " + eventType);
            // 点击更新 header
            LxList<LxModel> list = adapter.getCustomTypeData(Lx.VIEW_TYPE_HEADER);
            if (list != null) {
                list.updateSet(0, m -> {
                    CustomTypeData header = m.unpack();
                    header.desc = String.valueOf(System.currentTimeMillis());
                });
            }
            LxSelectComponent component = adapter.getComponent(LxSelectComponent.class);
            if (component != null) {
                component.select(model);
            }
        }
    }

    static class HeaderItemBind extends LxItemBind<CustomTypeData> {

        HeaderItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.desc_header;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_HEADER;
                opts.enableClick = true;
                opts.enableDbClick = true;
                opts.enableLongPress = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, CustomTypeData data, LxModel model, int position, @NonNull List<String> payloads) {
            holder.setText(R.id.desc_tv, data.desc)
                    .setImage(R.id.cover_iv, data.url, null);
        }

        @Override
        public void onEvent(LxContext context, CustomTypeData data, LxModel model, int eventType) {

            if (eventType == Lx.EVENT_LONG_PRESS) {
                // 长按删除 header
                LxList<LxModel> list = adapter.getCustomTypeData(Lx.VIEW_TYPE_HEADER);
                if (list != null) {
                    list.updateRemove(0);
                }
            }
            if (eventType == Lx.EVENT_CLICK) {
                // 点击删除内容第一个
                LxList<LxModel> list = adapter.getContentTypeData();
                list.updateClear();
            }
            if (eventType == Lx.EVENT_DOUBLE_CLICK) {
                // 双击更新第一个数据
                LxList<LxModel> list = adapter.getContentTypeData();
                if (list != null) {
                    list.updateSet(0, m -> {
                        if (m.getItemType() == TYPE_STUDENT) {
                            Student stu = m.unpack();
                            stu.name = "new stu " + System.currentTimeMillis();
                        } else if (m.getItemType() == TYPE_TEACHER) {
                            Teacher teacher = m.unpack();
                            teacher.name = "new teacher " + System.currentTimeMillis();
                        }
                    });
                }
            }
        }
    }


    static class FooterItemBind extends LxItemBind<CustomTypeData> {

        FooterItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.item_footer;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_FOOTER;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, CustomTypeData data, LxModel model, int position, @NonNull List<String> payloads) {
            holder.setText(R.id.desc_tv, data.desc);
        }

        @Override
        public void onEvent(LxContext context, CustomTypeData data, LxModel model, int eventType) {
            if (eventType == Lx.EVENT_LONG_PRESS) {
                // 长按删除 footer
                LxList<LxModel> list = adapter.getCustomTypeData(Lx.VIEW_TYPE_FOOTER);
                if (list != null) {
                    list.updateRemove(0);
                }
            }
            if (eventType == Lx.EVENT_CLICK) {
                // 点击删除内容第一个
                LxList<LxModel> list = adapter.getContentTypeData();
                if (list != null) {
                    list.updateClear();
                }
            }
            if (eventType == Lx.EVENT_DOUBLE_CLICK) {
                // 双击再加一个 footer
                LxList<LxModel> list = adapter.getCustomTypeData(Lx.VIEW_TYPE_FOOTER);
                if (list != null) {
                    list.updateAdd(LxTransformations.pack(Lx.VIEW_TYPE_FOOTER, new CustomTypeData("", String.valueOf(System.currentTimeMillis()))));
                }
            }
        }
    }

    class EmptyItemBind extends LxItemBind<CustomTypeData> {

        EmptyItemBind() {
            super(TypeOpts.make(opts -> {
                opts.layoutId = R.layout.empty_view;
                opts.spanSize = Lx.SPAN_SIZE_ALL;
                opts.viewType = Lx.VIEW_TYPE_EMPTY;
                opts.enableClick = true;
                opts.enableLongPress = true;
                opts.enableDbClick = true;
            }));
        }

        @Override
        public void onBindView(LxVh holder, CustomTypeData data, LxModel model, int position, @NonNull List<String> payloads) {
            holder.setClick(R.id.refresh_tv, v -> {
                setData();
            });
        }

        @Override
        public void onEvent(LxContext context, CustomTypeData data, LxModel model, int eventType) {

        }
    }
}

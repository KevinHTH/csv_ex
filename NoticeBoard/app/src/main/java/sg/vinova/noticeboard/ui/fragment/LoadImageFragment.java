package sg.vinova.noticeboard.ui.fragment;


import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.LoadImageZoomAdapter;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.utils.PreferencesUtils;


/**
 * A simple {@link Fragment} subclass.
 */


public class LoadImageFragment extends BaseMainFragment implements LoadImageZoomAdapter.ImageTapListener {

    @BindView(R.id.viewPagerImage)
    ViewPager viewPager;
    @BindView(R.id.imbBackImage)
    RoundedImageView imbBack;
    @BindView(R.id.imbNextImage)
    RoundedImageView imbNext;

    @BindView(R.id.tvAuthor)
    AppTextView tvAuthor;
    @BindView(R.id.tvCreateAt)
    AppTextView tvCreateAt;
    @BindView(R.id.toolbarView)
    FrameLayout toolbarView;
    @BindView(R.id.imbClose)
    ImageView imbClose;
    Unbinder unbinder;

    private int position;
    private int heightToolbar = 0;
    boolean isAlbum = false;
    private String transactionName;
    private AlphaAnimation fadOut, fadIn;
    private LoadImageZoomAdapter adapter;
    private List<String> imageUrls;
    private List<Description> photoList;
    Description currentItem;

    public LoadImageFragment() {
        // Required empty public constructor
    }

    public static LoadImageFragment newInstance(List<Description> photoList, int position) {
        LoadImageFragment imgFrag = new LoadImageFragment();
        imgFrag.getUrlsByList(photoList);
        imgFrag.position = position;
        imgFrag.currentItem = photoList.get(position);
        imgFrag.photoList = photoList;
        imgFrag.isAlbum = true;
        return imgFrag;
    }

    public static LoadImageFragment newInstance(int position, Description item) {

        LoadImageFragment imgFrag = new LoadImageFragment();
        imgFrag.position = position;
        imgFrag.currentItem = item;
        imgFrag.getUrlsByObj(item);
        imgFrag.isAlbum = false;
        return imgFrag;
    }

    public void setShareTransactionName(String name) {
        transactionName = name;
    }


    private void getUrlsByList(List<Description> photoList) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < photoList.size(); i++) {
            urls.add(photoList.get(i).getPhotoUrl());
        }
        this.imageUrls = urls;
    }

    private void getUrlsByObj(Description item) {
        List<String> urls = new ArrayList<>();
        if (item.getImages() != null) {
            for (int i = 0; i < item.getImages().size(); i++) {
                urls.add(item.getImages().get(i).getPhotoUrl());
            }
        } else {
            urls.add(item.getPhotoUrl());
        }

        this.imageUrls = urls;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getMainActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        fadIn = new AlphaAnimation(0.0f, 1.0f);
        fadIn.setDuration(300);
        fadOut = new AlphaAnimation(1.0f, 0.0f);
        fadOut.setDuration(300);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setTransitionName(transactionName);
        initView();
    }

    private void initView() {
        setupViewPager(viewPager);
        heightToolbar = getMainActivity().getSupportActionBar().getHeight();
        setupToolbar();
        getMainActivity().hideFooter(true);

        adapter.setTapListener(this);
        showHideBackNextButton(viewPager.getCurrentItem());
        if (adapter.getCount() == 1) {
            hideBackNext();
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                showHideBackNextButton(position);
                if (isAlbum) {
                    currentItem = photoList.get(position);
                    setupToolbar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void hideStatusBar(View view) {
        getMainActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showStatusBar(View view) {
        getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupToolbar() {
        try {
            getMainActivity().getToolbarHelper().showToolbar(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (currentItem != null) {
            tvAuthor.setText(currentItem.getUser().getUsername()+" "+currentItem.getUser().getPhoneNumber());
            tvCreateAt.setText(Utils.formatDate(currentItem.getCreatedAt(),
                    Constant.DATE_INPUT_FORMAT, Constant.DATE_OUTPUT_FORMAT));
            toolbarView.setPadding(0, Utils.getStatusBarHeight(getContext()), 0, 0);
        }
    }

    @OnClick(R.id.imbClose)
    public void onClickClose() {
        getMainActivity().getFragmentHelper().popBackStack();
    }

    private void showHideBackNextButton(int position) {
        if (position == 0) {
            // hide back - show next
            if (imbBack.getVisibility() == View.VISIBLE) {
                imbBack.startAnimation(fadOut);
            }
            imbBack.setVisibility(View.INVISIBLE);
            if (imbNext.getVisibility() == View.INVISIBLE) {
                imbNext.startAnimation(fadIn);
            }
            imbNext.setVisibility(View.VISIBLE);
        } else if (position == (adapter.getCount() - 1)) {
            // show back - hide next
            if (imbBack.getVisibility() == View.INVISIBLE) {
                imbBack.startAnimation(fadIn);
            }
            imbBack.setVisibility(View.VISIBLE);
            if (imbNext.getVisibility() == View.VISIBLE) {
                imbNext.startAnimation(fadOut);
            }
            imbNext.setVisibility(View.INVISIBLE);
        } else {
            showBackNext();
        }
    }

    private void showBackNext() {
        if (imbBack.getVisibility() == View.INVISIBLE) {
            imbBack.startAnimation(fadIn);
        }
        imbBack.setVisibility(View.VISIBLE);
        if (imbNext.getVisibility() == View.INVISIBLE) {
            imbNext.startAnimation(fadIn);
        }
        imbNext.setVisibility(View.VISIBLE);
    }

    private void hideBackNext() {
        if (imbBack.getVisibility() == View.VISIBLE) {
            imbBack.startAnimation(fadOut);
            imbBack.setVisibility(View.INVISIBLE);
        }
        if (imbNext.getVisibility() == View.VISIBLE) {
            imbNext.startAnimation(fadOut);
            imbNext.setVisibility(View.INVISIBLE);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new LoadImageZoomAdapter(getContext(), imageUrls);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    @OnClick(R.id.imbBackImage)
    public void onBackClick() {
        int current = viewPager.getCurrentItem();
        if (current > 0 && current < adapter.getCount()) {
            viewPager.setCurrentItem(current - 1, true);
        }
    }

    @OnClick(R.id.imbNextImage)
    public void onNextClick() {
        int current = viewPager.getCurrentItem();
        if (current < adapter.getCount()) {
            viewPager.setCurrentItem(current + 1, true);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_photo_view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
            getMainActivity().getMainToolbarHelper().showToolbar(true);
            getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
            getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);
            getMainActivity().hideFooter(false);
            getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
        unbinder.unbind();
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getMainActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), color));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMainActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setOnPhotoTap() {
        toolbarView.animate().setDuration(300).translationY(-heightToolbar);
        if (adapter.getCount() == 1) {
            return;
        }
        hideBackNext();
    }

    @Override
    public void setOutPhotoTap() {
        toolbarView.animate().setDuration(300).translationY(0);
        if (adapter.getCount() == 1) {
            return;
        }
        showHideBackNextButton(viewPager.getCurrentItem());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}

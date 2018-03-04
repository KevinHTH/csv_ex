package sg.vinova.noticeboard.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.PhotoAdapter;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.module.user.FeedbackPresenter;
import sg.vinova.noticeboard.module.user.FeedbackPresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;

/**
 * Created by Jacky on 4/26/17.
 */

public class NewFeedbackFragment extends BaseMainFragment implements FeedbackPresenter.View, PhotoAdapter.OnClickRemovePhoto {
    @BindView(R.id.edtDescription)
    AppEditText edtDescription;
    @BindView(R.id.rvPhoto)
    RecyclerView rvPhoto;
    @BindView(R.id.imgTackPhoto)
    ImageView imgTackPhoto;
    @BindView(R.id.imgAttachPhoto)
    ImageView imgAttachPhoto;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvNote)
    AppTextView tvNote;

    Unbinder unbinder;

    private FeedbackPresenterImpl feedbackPresenter;
    private PhotoAdapter photoAdapter;
    private String pathPhoto;
    private String type;


    public static NewFeedbackFragment newInstance(String type) {
        NewFeedbackFragment fragment = new NewFeedbackFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedbackPresenter = new FeedbackPresenterImpl(getBaseAppActivity());
        photoAdapter = new PhotoAdapter(getMainActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, viewRoot);
        feedbackPresenter.bind(this);
        setUpPhoto();
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionbar();
        getMainActivity().hideFooter(true);
        tvSubmit.setText(R.string.send);
        if (type.equals(ConstantApp.FEEDBACK.FEEDBACK_MCST)) {
            edtDescription.setHint("feedback to mcst");
            setTitleToolbar("Give feedback");
            tvNote.setVisibility(View.GONE);
        }
        if (type.equals(ConstantApp.FEEDBACK.FEEDBACK_APP)) {
            setTitleToolbar("Give Feedback");
            tvNote.setVisibility(View.VISIBLE);
        }
        photoAdapter.setOnClickRemovePhoto(this);
    }

    private void setUpPhoto() {
        LinearLayoutManager lmm = new LinearLayoutManager(getContext());
        rvPhoto.setLayoutManager(lmm);
        rvPhoto.setAdapter(photoAdapter);
    }

    private void setupActionbar() {
        try {
            getBaseActivity().getToolbarHelper().showToolbar(true);
            getBaseActivity().getToolbarHelper().setImageForLeftButton(R.mipmap.back);
            getBaseActivity().getToolbarHelper().setRightToolbarButton(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imgTackPhoto)
    public void onClickTakePhoto() {
        new RxPermissions(getMainActivity()).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .filter(aBoolean -> aBoolean)
                .subscribe(aBoolean -> {
                    pathPhoto = Utils.takePicture(getMainActivity());
                });
    }

    @OnClick(R.id.imgAttachPhoto)
    public void onClickAttachPhoto() {
        feedbackPresenter.attachPhoto();
    }

    @OnClick(R.id.tvSubmit)
    public void onClickSend() {
        if (TextUtils.isEmpty(edtDescription.getText())) {
            edtDescription.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.description)), "OK");
            return;
        }
        feedbackPresenter.sendFeedback(edtDescription.getText().toString(), type, pathPhoto);
    }


    private void sendEmail(String text, String pathPhoto) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(pathPhoto)) {
            Uri fileUri = FileProvider.getUriForFile(getContext(), "com.myfileprovider", new File(pathPhoto));
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        }
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                ConstantApp.EMAIL_ADDRESS});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Tack it");
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), Constant.REQEST_SENDMAIL);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_feedback;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        feedbackPresenter.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQEST_SENDMAIL) {
            getBaseAppActivity().onBackPressed();
        }

        if (resultCode != Activity.RESULT_OK) {
            return;
        }


        if (requestCode == Constant.REQUEST_TAKE_PHOTO) {
            Observable.just(BitmapFactory.decodeFile(pathPhoto))
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<Bitmap, Bitmap>() {
                        @Override
                        public Bitmap call(Bitmap bitmap) {
                            Bitmap rotatedBitmap = Utils.getRotatedBitmap(pathPhoto, bitmap);
                            return Utils.scaleBitmap(rotatedBitmap, 800);
                        }
                    })
                    .flatMap(new Func1<Bitmap, Observable<String>>() {
                        @Override
                        public Observable<String> call(Bitmap bitmap) {
                            return Observable.just(Utils.saveBitmap(getContext(), String.valueOf(Calendar.getInstance().getTimeInMillis()), bitmap));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String path) {
                            if (isAdded()) {
                                Photo photo = new Photo(path, true);
                                onTakePhotoSuccess(photo);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (isAdded()) {
                                onError(throwable.getMessage());
                            }
                        }
                    });
        }

    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onAttachPhotoSuccess(String path) {
        pathPhoto = path;
        Photo p = new Photo(path, true);
        photoAdapter.clear();
        photoAdapter.add(p);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTakePhotoSuccess(Photo photo) {
        photoAdapter.clear();
        photoAdapter.add(photo);
        photoAdapter.notifyDataSetChanged();
        pathPhoto = photo.getPath();
    }

    @Override
    public void onSendFeedbackSuccess(String message) {
        SnackBarUtils.getSnackInstance().showSuccess(getView(), message);
        getBaseAppActivity().onBackPressed();
    }

    @Override
    public void onClickRemove(Photo photoModel) {
        pathPhoto = null;
    }
}

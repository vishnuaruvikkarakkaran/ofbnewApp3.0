package com.enfin.ofabee3.ui.module.coursedetail.coursedescription;


import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enfin.ofabee3.R;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.BundleDetailResponseModel;
import com.enfin.ofabee3.data.remote.model.coursedetail.response.CourseDetailResponseModel;
import com.enfin.ofabee3.utils.TextJustification;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDescriptionFragment extends Fragment {

    private CourseDetailResponseModel courseDetailResponseModel;
    private BundleDetailResponseModel bundleDetailResponseModel;
    private TextView whatugetTV, whatugetDesTV, descriptionTV, requirementTV, requirementDesTV;
    private WebView descriptionDesTV;
    private LinearLayout wugLinearLayout, reqLinearLayout, wugParent, reqParent, desParent;
    private static List<String> HTML_TEXT_WUG = new ArrayList<>();
    private static List<String> HTML_TEXT_REQUIRENT = new ArrayList<>();
    private static String HTML_TEXT_DES;

    static Point size;
    static float density;

    private String[] wugList = new String[]{"To secure them from Hackers, Cyber-criminals, Scammers and Thief", "To reduce data and information loss", "To protect social accounts, bank accounts, email accounts and other type of online accounts."};

    public CourseDescriptionFragment(CourseDetailResponseModel data) {
        this.courseDetailResponseModel = data;
    }

    public CourseDescriptionFragment(BundleDetailResponseModel data) {
        this.bundleDetailResponseModel = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_course_description, container, false);
        whatugetTV = rootview.findViewById(R.id.whatuget_tv);
        whatugetDesTV = rootview.findViewById(R.id.whatuget_des_tv);
        descriptionTV = rootview.findViewById(R.id.description_tv);
        descriptionDesTV = rootview.findViewById(R.id.description_des_tv);
        requirementTV = rootview.findViewById(R.id.requirement_tv);
        requirementDesTV = rootview.findViewById(R.id.requirement_des_tv);
        wugLinearLayout = rootview.findViewById(R.id.wug_container);
        reqLinearLayout = rootview.findViewById(R.id.req_container);
        wugParent = rootview.findViewById(R.id.wug_parent_ll);
        reqParent = rootview.findViewById(R.id.req_parent_ll);
        desParent = rootview.findViewById(R.id.des_parent_ll);
        HTML_TEXT_REQUIRENT.clear();
        HTML_TEXT_WUG.clear();
        HTML_TEXT_DES = "";
        findRezloution();
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/opensans_regular.ttf");
        if (courseDetailResponseModel != null) {
            HTML_TEXT_DES = "<html><body style='text-align:justify;color:#222222;'>" +
                    courseDetailResponseModel.getData().getCb_description()
                    + "</html>";
            //HTML_TEXT_DES = courseDetailResponseModel.getData().getCb_description();

            if (courseDetailResponseModel.getData().getCb_what_u_get() != null)
                HTML_TEXT_WUG.addAll(courseDetailResponseModel.getData().getCb_what_u_get());
            else {
                wugLinearLayout.setVisibility(View.GONE);
                whatugetTV.setVisibility(View.GONE);
                wugParent.setVisibility(View.GONE);
            }
            if (courseDetailResponseModel.getData().getCb_requirements() != null)
                HTML_TEXT_REQUIRENT.addAll(courseDetailResponseModel.getData().getCb_requirements());
            else {
                reqLinearLayout.setVisibility(View.GONE);
                requirementTV.setVisibility(View.GONE);
                reqParent.setVisibility(View.GONE);
            }
            for (int i = 0; i < HTML_TEXT_WUG.size(); i++) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.course_description_item, wugLinearLayout, false);
                TextView textView = v.findViewById(R.id.textview);
                textView.setText(HTML_TEXT_WUG.get(i));
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(16);
                textView.setTypeface(typeface);
                textView.setPadding(30, 10, 30, 10);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                //TextJustification.justify(textView,size.x);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                } else {
                }
                wugLinearLayout.addView(v);
            }
            for (int i = 0; i < HTML_TEXT_REQUIRENT.size(); i++) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.course_description_item, wugLinearLayout, false);
                TextView textView = v.findViewById(R.id.textview);
                textView.setText(HTML_TEXT_REQUIRENT.get(i));
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(16);
                textView.setTypeface(typeface);
                textView.setPadding(30, 10, 30, 10);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                //TextJustification.justify(textView,size.x);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                } else {
                }
                reqLinearLayout.addView(v);
            }
        } else if (bundleDetailResponseModel != null) {
            HTML_TEXT_DES = "<html><body style='text-align:justify;color:#222222;'>"+
                    bundleDetailResponseModel.getData().getC_description()
                    + "</html>";
            //HTML_TEXT_DES = bundleDetailResponseModel.getData().getC_description();
            //HTML_TEXT_WUG = bundleDetailResponseModel.getData().getC_what_u_get();
            //HTML_TEXT_REQUIRENT = bundleDetailResponseModel.getData().getC_requirements();
            wugParent.setVisibility(View.GONE);
            reqParent.setVisibility(View.GONE);
            whatugetDesTV.setVisibility(View.GONE);
            whatugetTV.setVisibility(View.GONE);
            requirementDesTV.setVisibility(View.GONE);
            requirementTV.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!TextUtils.isEmpty(HTML_TEXT_DES) && !HTML_TEXT_DES.equalsIgnoreCase(" ") && !HTML_TEXT_DES.equalsIgnoreCase("null")) {
                //descriptionDesTV.setText(Html.fromHtml(HTML_TEXT_DES, Html.FROM_HTML_MODE_COMPACT));
                descriptionDesTV.getSettings().setJavaScriptEnabled(true);
                descriptionDesTV.loadData(HTML_TEXT_DES, "text/html; charset=utf-8","UTF-8");
                //descriptionDesTV.loadData(HTML_TEXT_DES, "text/html; charset=utf-8","UTF-8");
                descriptionDesTV.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + HTML_TEXT_DES, "text/html; charset=utf-8", "UTF-8", null);
                //TextJustification.justify(descriptionDesTV,size.x);
                /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    descriptionDesTV.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                } else {
                }*/
            } else {
                desParent.setVisibility(View.GONE);
                descriptionDesTV.setVisibility(View.GONE);
                descriptionTV.setVisibility(View.GONE);
            }
        } else {

            if (!TextUtils.isEmpty(HTML_TEXT_DES) && !HTML_TEXT_DES.equalsIgnoreCase(" ") && !HTML_TEXT_DES.equalsIgnoreCase("null")) {
                //descriptionDesTV.setText(Html.fromHtml(HTML_TEXT_DES));
                descriptionDesTV.getSettings().setJavaScriptEnabled(true);
                //descriptionDesTV.loadData(HTML_TEXT_DES, "text/html; charset=utf-8","UTF-8");
                descriptionDesTV.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + HTML_TEXT_DES, "text/html; charset=utf-8", "UTF-8", null);
                //TextJustification.justify(descriptionDesTV,size.x);
                /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    descriptionDesTV.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
                } else {
                }*/
            } else {
                desParent.setVisibility(View.GONE);
                descriptionDesTV.setVisibility(View.GONE);
                descriptionTV.setVisibility(View.GONE);
            }
        }
        return rootview;
    }

    private void findRezloution() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        density = dm.density;
        display.getSize(size);
    }
}

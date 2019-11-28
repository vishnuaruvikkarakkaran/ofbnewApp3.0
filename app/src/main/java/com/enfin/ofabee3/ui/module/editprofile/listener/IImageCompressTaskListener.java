package com.enfin.ofabee3.ui.module.editprofile.listener;

import java.io.File;
import java.util.List;

public interface IImageCompressTaskListener {

    public void onComplete(List<File> compressed);

    public void onError(Throwable error);

}

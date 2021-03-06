package com.fastaccess.ui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.model.Repo;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.RoundBackgroundSpan;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.fastaccess.ui.widgets.color.ColorGenerator;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.text.NumberFormat;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 */

public class ReposViewHolder extends BaseViewHolder<Repo> {

    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.stars) FontTextView stars;
    @BindView(R.id.forks) FontTextView forks;
    @BindView(R.id.language) FontTextView language;
    @Nullable @BindView(R.id.avatarLayout) AvatarLayout avatarLayout;
    @BindString(R.string.forked) String forked;
    @BindString(R.string.private_repo) String privateRepo;
    @BindColor(R.color.material_indigo_700) int forkColor;
    @BindColor(R.color.material_deep_purple_700) int privateColor;
    private boolean isStarred;
    private boolean withImage;

    private ReposViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter, boolean isStarred, boolean withImage) {
        super(itemView, adapter);
        this.isStarred = isStarred;
        this.withImage = withImage;
    }

    public static ReposViewHolder newInstance(ViewGroup viewGroup, BaseRecyclerAdapter adapter, boolean isStarred, boolean withImage) {
        if (withImage) {
            return new ReposViewHolder(getView(viewGroup, R.layout.repos_row_item), adapter, isStarred, true);
        } else {
            return new ReposViewHolder(getView(viewGroup, R.layout.repos_row_no_image_item), adapter, isStarred, false);
        }

    }

    @Override public void bind(@NonNull Repo repo) {
        if (repo.isFork() && !isStarred) {
            title.setText(SpannableBuilder.builder()
                    .append(" " + forked + " ", new RoundBackgroundSpan(forkColor, 5))
                    .append(" ")
                    .append(repo.getName()));
        } else if (repo.isPrivateX()) {
            title.setText(SpannableBuilder.builder()
                    .append(" " + privateRepo + " ", new RoundBackgroundSpan(privateColor, 5))
                    .append(" ")
                    .append(repo.getName()));
        } else {
            title.setText(!isStarred ? repo.getName() : repo.getFullName());
        }
        if (withImage) {
            String avatar = repo.getOwner() != null ? repo.getOwner().getAvatarUrl() : null;
            String login = repo.getOwner() != null ? repo.getOwner().getLogin() : null;
            if (avatarLayout != null) {
                avatarLayout.setVisibility(View.VISIBLE);
                avatarLayout.setUrl(avatar, login);
            }
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        stars.setText(numberFormat.format(repo.getStargazersCount()));
        forks.setText(numberFormat.format(repo.getForks()));
        date.setText(ParseDateFormat.getTimeAgo(repo.getUpdatedAt()));
        if (!InputHelper.isEmpty(repo.getLanguage())) {
            language.setText(repo.getLanguage());
            language.setTextColor(ColorGenerator.MATERIAL.getColor(repo.getLanguage()));
            language.setVisibility(View.VISIBLE);
        }
    }
}

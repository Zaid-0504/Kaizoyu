package com.astarivi.kaizoyu.gui.home;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.astarivi.kaizoyu.core.rss.RssFetcher;
import com.astarivi.kaizoyu.databinding.FragmentHomeBinding;
import com.astarivi.kaizoyu.gui.home.recycler.recommendations.MainCategoryContainer;
import com.astarivi.kaizoyu.utils.Threading;
import com.rometools.rome.feed.synd.SyndEntry;

import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import lombok.Getter;


public class HomeViewModel extends ViewModel {
    @Getter
    private final MutableLiveData<ArrayList<MainCategoryContainer>> containers = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<List<SyndEntry>> news = new MutableLiveData<>();
    private Future<?> reloadFuture = null;

    public void reloadHome(FragmentHomeBinding binding) {
        if (reloadFuture != null && !reloadFuture.isDone()) return;
        binding.newsRecycler.setVisibility(View.INVISIBLE);
        binding.itemsLayout.setVisibility(View.INVISIBLE);
        binding.loadingBar.setVisibility(View.VISIBLE);
        binding.newsLoading.setVisibility(View.VISIBLE);
        binding.newsHeader.setVisibility(View.VISIBLE);
        binding.noResultsMessage.setVisibility(View.GONE);

        containers.postValue(new ArrayList<>());

        fetchHome();
    }

    private void fetchHome() {
        reloadFuture = Threading.submitTask(Threading.TASK.INSTANT,() -> {
                try {
                    news.postValue(
                            RssFetcher.getANNFeed()
                    );
                } catch (Exception e) {
                    news.postValue(null);
                    Logger.error(e);
                }

//                Kitsu kitsu = new Kitsu();
//
//                fetchCategory(
//                        kitsu,
//                        R.string.popular_anime,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.home_beloved,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "-favoritesCount"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.home_seinen,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "filter[categories]",
//                                        "seinen"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "-favoritesCount"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.popular_airing,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "filter[subtype]",
//                                        "TV"
//                                ).
//                                setCustomParameter(
//                                        "filter[status]",
//                                        "current"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.popular_upcoming,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "filter[subtype]",
//                                        "TV"
//                                ).
//                                setCustomParameter(
//                                        "filter[status]",
//                                        "upcoming"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.trash_anime,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "filter[subtype]",
//                                        "TV"
//                                ).
//                                setCustomParameter(
//                                        "filter[status]",
//                                        "finished"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "averageRating"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.shoujo_anime,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        15
//                                ).
//                                setCustomParameter(
//                                        "filter[categories]",
//                                        "shoujo"
//                                ).
//                                setCustomParameter(
//                                        "filter[subtype]",
//                                        "TV"
//                                ).
//                                setCustomParameter(
//                                        "filter[status]",
//                                        "finished"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                )
//                );
//
//                fetchCategory(
//                        kitsu,
//                        R.string.shounen_anime,
//                        new KitsuSearchParams().
//                                setLimit(
//                                        20
//                                ).
//                                setCustomParameter(
//                                        "filter[categories]",
//                                        "shounen"
//                                ).
//                                setCustomParameter(
//                                        "filter[subtype]",
//                                        "TV"
//                                ).
//                                setCustomParameter(
//                                        "filter[status]",
//                                        "finished"
//                                ).
//                                setCustomParameter(
//                                        "sort",
//                                        "popularityRank"
//                                )
//                );
//
//                @Nullable ArrayList<MainCategoryContainer> items = containers.getValue();
//
//                if (items == null || items.isEmpty()) {
//                    containers.postValue(null);
//                }
            }
        );
    }

//    private void fetchCategory(Kitsu kitsu, @StringRes int titleResourceId, KitsuSearchParams params) {
//        List<AniListAnime> anime;
//        try {
//            anime = kitsu.searchAnime(params);
//        } catch (Exception ignored) {
//            return;
//        }
//
//        List<Anime> fetchedAnime = new Anime.BulkAnimeBuilder(anime).build();
//        anime.clear();
//        addItemToMutable(new MainCategoryContainer(
//                titleResourceId,
//                fetchedAnime
//        ));
//    }

    private synchronized void addItemToMutable(MainCategoryContainer item) {
        @Nullable ArrayList<MainCategoryContainer> items = containers.getValue();

        if (items == null) {
            ArrayList<MainCategoryContainer> newItems = new ArrayList<>();
            newItems.add(item);
            containers.postValue(newItems);
            return;
        }

        items.add(item);
        containers.postValue(items);
    }
}

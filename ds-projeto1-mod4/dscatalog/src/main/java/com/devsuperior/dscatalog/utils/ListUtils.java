package com.devsuperior.dscatalog.utils;

import com.devsuperior.dscatalog.entities.ID;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ListUtils {

    public static <E> List<? extends ID<E>> orderBy(
        List<? extends ID<E>> orderedList, List<? extends ID<E>> unorderedList
    ) {
        List<ID<E>> response = new ArrayList<>();

        Map<E, ID<E>> itemsMap = new HashMap<>();

        for (ID<E> obj : unorderedList) {
            itemsMap.put(obj.getId(), obj);
        }

        for (ID<E> obj : orderedList) {
            response.add(itemsMap.get(obj.getId()));
        }

        return response;
    }

}

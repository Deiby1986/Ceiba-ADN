package com.ventas.ventadepasajes.domain.port.repository;

import com.ventas.ventadepasajes.domain.model.entity.Purchase;

import java.util.List;

public interface RepositoryPurchase {

    Purchase createPurchase(Purchase purchase);

    List<Purchase> listPurchase();

    boolean deletePurchase(long id);

    Purchase updatePurchase(long id, Purchase purchase);
}

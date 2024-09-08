package br.com.emerson.core.dataprovider;

import br.com.emerson.core.entity.CartaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CartaoRepository implements PanacheRepositoryBase<CartaoEntity, UUID> {
}

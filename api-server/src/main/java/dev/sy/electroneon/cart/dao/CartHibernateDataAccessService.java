package dev.sy.electroneon.cart.dao;

import dev.sy.electroneon.cart.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CartHibernateDataAccessService implements CartDao {

    @PersistenceContext
    private EntityManager em;

    public CartHibernateDataAccessService() {
    }

    @Override
    public Optional<Cart> selectCartById(UUID cartId) {
        return Optional.ofNullable(
                em.find(Cart.class, cartId)
        );
    }

    @Override
    public List<Cart> selectCartsByUserId(UUID userId) {
        String jpql = "SELECT c FROM Cart c WHERE c.user.id = :userId";

        return em
                .createQuery(jpql, Cart.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public boolean existsCartById(UUID cartId) {
        return selectCartById(cartId).isPresent();
    }

    @Override
    // NB Use `@Transactional` i/o `em.getTransaction()` when using
    // `@PersistenceContext private EntityManager`, @see:
    // https://www.baeldung.com/jpa-hibernate-persistence-context
    @Transactional
    public void insertCart(Cart cart) {
        em.persist(cart);
    }

    /**
     * Implementation of {@link CartDao#updateCart(Cart)} method by using
     * Hibernate entity operations.
     * <p> This method
     * <a href="https://stackoverflow.com/questions/21175450/merge-vs-find-to-update-entities-jpa">
     * avoid the detached object and the merge operation entirely by locating
     * the managed version and manually copying the changes into it.
     * </a>
     * <p>
     * This design choice is based on (1) that the amount of information
     * being updated is very small, and (2) that there should be no changes
     * to the associated entities ({@code User} and {@code Product} objects)
     * during the process of interacting with the shopping cart.
     * <p>
     * Alternative approaches are (1) reloading/re-retrieving associated
     * entities and (2) detaching associated entities.
     * <p>
     * Reloading associated entities  ensures that any changes made to these
     * associated entities elsewhere are not unintentionally persisted along
     * with the {@code Cart} entity. However, it requires additional database
     * queries to reload the associated entities, which may impact performance,
     * especially if there are many associated entities or if the database
     * queries are complex.
     * <p>
     * Detaching the {@code Cart} entity retrieved from the database before
     * updating it means that any changes made ot the associated entities in
     * the {@code update} parameter are ignored, and only the state of the
     * {@code Cart} entity itself is persisted, in this case, only the {@code
     * id}, {@code quantity}, and the {@code id} of the associated entities
     * will remain in the detached cart instance. While this approach avoids
     * additional database queries to reload associated entities, it may lead
     * to unintended behavior if there are changes to associated entities
     * that need to be persisted, which is not the case during the user
     * interactions with the shopping cart.
     * <p>
     * Similarly, <code>CascadeType.PERSIST</code> is an unsuitable option
     * when annotating the relationship between the {@code Cart} entity and its
     * associations. This is because it will attempt to persist the associations
     * when persisting {@code Cart} entity, potentially causing conflicts or
     * data inconsistencies.
     *
     * @param update {@link Cart} object used for further update operation
     * @see <a href="https://stackoverflow.com/questions/60112439/what-is-the-difference-between-evict-and-detach-in-hibernate">
     * <code>EntityManager.detach</code> vs. <code>Session.evict</code>
     * </a>
     * @see <a href="https://stackoverflow.com/questions/5640778/hibernate-sessionfactory-vs-jpa-entitymanagerfactory">
     * <code>EntityManger</code> vs. <code>Session</code>
     * </a>
     * @see <a href="https://www.baeldung.com/hibernate-save-persist-update-merge-saveorupdate">
     * <code>EventManager.merge</code> and <code>Session.merge</code>
     * </a>
     */
    @Override
    @Transactional
    public void updateCart(Cart update) {
        // Destructure the `update` object
        UUID cartId = update.getId();
        Integer quantity = update.getQuantity();

        Optional<Cart> optionalCart = Optional.ofNullable(
                em.find(Cart.class, cartId)
        );
        optionalCart.ifPresent(cart -> cart.setQuantity(quantity));
    }

    @Override
    @Transactional
    public void incrementCartQty(UUID cartId) {
        Optional<Cart> optionalCart = Optional.ofNullable(
                em.find(Cart.class, cartId)
        );
        optionalCart.ifPresent(cart -> {
            Integer newQuantity = cart.getQuantity() + 1;

            cart.setQuantity(newQuantity);
        });
    }

    @Override
    @Transactional
    public void decrementCartQty(UUID cartId) {
        Optional<Cart> optionalCart = Optional.ofNullable(
                em.find(Cart.class, cartId)
        );
        optionalCart.ifPresent(cart -> {
            Integer newQuantity = cart.getQuantity() - 1;

            cart.setQuantity(newQuantity);
        });
    }

    @Override
    @Transactional
    public void deleteCartById(UUID cartId) {
        Optional<Cart> optionalCart = Optional.ofNullable(
                em.find(Cart.class, cartId)
        );
        optionalCart.ifPresent(em::remove);
    }
}

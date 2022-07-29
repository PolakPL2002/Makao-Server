package tech.kucharski.makao.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.kucharski.makao.util.JSONConvertible;

import java.util.*;

/**
 * Deck of cards
 */
public class Deck implements JSONConvertible {
    private final List<Card> cards = Collections.synchronizedList(new ArrayList<>());
    private final List<UUID> discardedCards = Collections.synchronizedList(new ArrayList<>());
    private final Map<UUID, List<UUID>> playersCards = Collections.synchronizedMap(new HashMap<>());
    private final List<UUID> remainingCards = Collections.synchronizedList(new ArrayList<>());

    /**
     * @param numberOfDecks Number of decks of cards to use
     */
    public Deck(int numberOfDecks) {
        if (numberOfDecks < 1) numberOfDecks = 1;

        for (int i = 0; i < numberOfDecks; i++) {
            for (Card.CardType type : Card.CardType.values())
                cards.add(new Card(uniqueUUID(), type));
        }

        //Add all cards to unused stack
        for (Card card : cards)
            remainingCards.add(card.getUUID());

        //Shuffle cards
        Collections.shuffle(remainingCards);

        discardedCards.add(remainingCards.remove(0));
        while (!isNormal(discardedCards.get(discardedCards.size() - 1))) {
            discardedCards.add(remainingCards.remove(0));
        }
    }

    /**
     * @param cardUUID Card UUID
     * @return Whether card is not special
     */
    public boolean isNormal(@NotNull UUID cardUUID) {
        final Card.CardType type = Objects.requireNonNull(getCardByUUID(cardUUID)).getType();
        final Card.CardType.Value value = type.getValue();
        final Card.CardType.Color color = type.getColor();
        return value == Card.CardType.Value.CARD_0 ||
                value == Card.CardType.Value.CARD_1 ||
                value == Card.CardType.Value.CARD_5 ||
                value == Card.CardType.Value.CARD_6 ||
                value == Card.CardType.Value.CARD_7 ||
                value == Card.CardType.Value.CARD_8 ||
                value == Card.CardType.Value.CARD_9 ||
                value == Card.CardType.Value.CARD_10 ||
                value == Card.CardType.Value.CARD_11 ||
                (value == Card.CardType.Value.CARD_KING && (color == Card.CardType.Color.CLUBS || color == Card.CardType.Color.DIAMONDS));
    }

    /**
     * @return Random unique UUID
     */
    @NotNull
    private UUID uniqueUUID() {
        UUID uuid = UUID.randomUUID();
        while (getCardByUUID(uuid) != null)
            uuid = UUID.randomUUID();
        return uuid;
    }

    /**
     * @param uuid Card UUID
     * @return Card
     */
    @Nullable
    public Card getCardByUUID(@NotNull UUID uuid) {
        //TODO Add cache
        for (Card card : cards)
            if (card.getUUID().equals(uuid))
                return card;
        return null;
    }

    /**
     * @return All deck cards
     */
    @NotNull
    public Card[] getCards() {
        return cards.toArray(new Card[0]);
    }

    /**
     * @return Current top card
     */
    @NotNull
    public Card getCurrentTopCard() {
        return Objects.requireNonNull(getCardByUUID(discardedCards.get(discardedCards.size() - 1)));
    }

    /**
     * @param uuid     Player UUID
     * @param numCards Number of cards to give
     */
    public void givePlayerCards(@NotNull UUID uuid, int numCards) {
        checkPlayerExists(uuid);
        final List<UUID> playerCards = playersCards.get(uuid);
        for (int i = 0; i < numCards; i++) {
            if (remainingCards.size() == 0)
                shuffleUsed();
            if (remainingCards.size() == 0)
                continue;
            playerCards.add(remainingCards.remove(0));
        }
    }

    /**
     * @param uuid Player UUID
     */
    private void checkPlayerExists(@NotNull UUID uuid) {
        if (!playersCards.containsKey(uuid)) {
            playersCards.put(uuid, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    /**
     * Re-shuffles used cards and puts them into unused stack.
     */
    private void shuffleUsed() {
        if (discardedCards.size() < 2)
            return;
        ArrayList<UUID> toAdd = new ArrayList<>();
        while (discardedCards.size() > 1)
            toAdd.add(discardedCards.remove(0));
        Collections.shuffle(toAdd);
        remainingCards.addAll(toAdd);
    }

    /**
     * Plays a card.
     *
     * @param card Card to be played
     */
    public void playCard(@NotNull Card card) {
        if (!cards.contains(card))
            return;
        for (List<UUID> value : playersCards.values()) {
            value.remove(card.getUUID());
        }
        discardedCards.add(card.getUUID());
    }

    /**
     * @param player Player UUID
     * @param color  Card color
     * @return Whether player has a card of this color
     */
    public boolean playerHasCard(@NotNull UUID player, @NotNull Card.CardType.Color color) {
        checkPlayerExists(player);
        final List<UUID> playerCards = getPlayerCards(player);
        for (Card card : playerCards.stream().map(this::getCardByUUID).toList())
            if (card.getType().getColor() == color)
                return true;
        return false;
    }

    /**
     * @param uuid Player UUID
     * @return Player cards
     */
    @NotNull
    public List<UUID> getPlayerCards(@NotNull UUID uuid) {
        checkPlayerExists(uuid);

        return new ArrayList<>(playersCards.get(uuid));
    }

    /**
     * @param player Player UUID
     * @param value  Card value
     * @return Whether player has a card of this value
     */
    public boolean playerHasCard(@NotNull UUID player, @NotNull Card.CardType.Value value) {
        checkPlayerExists(player);
        final List<UUID> playerCards = getPlayerCards(player);
        for (Card card : playerCards.stream().map(this::getCardByUUID).toList())
            if (card.getType().getValue() == value)
                return true;
        return false;
    }

    /**
     * @param player Player UUID
     * @param color  Card color
     * @param value  Card value
     * @return Whether player has a card of this color and value
     */
    public boolean playerHasCard(@NotNull UUID player, @NotNull Card.CardType.Color color, @NotNull Card.CardType.Value value) {
        checkPlayerExists(player);
        final List<UUID> playerCards = getPlayerCards(player);
        for (Card card : playerCards.stream().map(this::getCardByUUID).toList())
            if (card.getType().getColor() == color && card.getType().getValue() == value)
                return true;
        return false;
    }

    /**
     * @param playerUUID Player to be removed.
     */
    public void removePlayer(UUID playerUUID) {
        discardedCards.addAll(playersCards.get(playerUUID));
        playersCards.remove(playerUUID);
    }

    /**
     * @return JSON representation of the object.
     */
    @Override
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();

        JsonArray cards = new JsonArray();
        this.cards.forEach(card -> cards.add(card.toJSONObject()));

        obj.add("cards", cards);

        JsonArray discardedCards = new JsonArray();
        this.discardedCards.forEach(uuid -> discardedCards.add(uuid.toString()));

        obj.add("discardedCards", discardedCards);
        return obj;
    }

    /**
     * @return Simplified JSON data.
     */
    public JsonElement toSimpleJSONObject() {
        JsonObject obj = new JsonObject();

        JsonArray discardedCards = new JsonArray();
        this.discardedCards.forEach(uuid -> discardedCards.add(uuid.toString()));

        obj.add("discardedCards", discardedCards);
        return obj;
    }

    /**
     * @return Number of cards in unused pile
     */
    public int unusedCount() {
        return remainingCards.size();
    }

    /**
     * @return Number of cards in used pile
     */
    public int usedCount() {
        return discardedCards.size();
    }
}

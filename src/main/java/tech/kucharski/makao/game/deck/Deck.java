package tech.kucharski.makao.game.deck;

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
    private final Map<CardType, CardSettings> cardSettings;
    private final List<Card> cards = Collections.synchronizedList(new ArrayList<>());
    private final List<UUID> discardedCards = Collections.synchronizedList(new ArrayList<>());
    private final Map<UUID, List<UUID>> playersCards = Collections.synchronizedMap(new HashMap<>());
    private final List<UUID> remainingCards = Collections.synchronizedList(new ArrayList<>());

    /**
     * @param numberOfDecks Number of decks of cards to use
     * @param cardSettings  Card settings to be used with the deck
     */
    public Deck(int numberOfDecks, Map<CardType, CardSettings> cardSettings) {
        this.cardSettings = cardSettings;
        if (numberOfDecks < 1) numberOfDecks = 1;

        for (int i = 0; i < numberOfDecks; i++) {
            for (CardType type : CardType.values())
                cards.add(new Card(uniqueUUID(), type));
        }

        //Add all cards to unused stack
        for (Card card : cards)
            remainingCards.add(card.getUUID());

        //Shuffle cards
        Collections.shuffle(remainingCards);

        discardedCards.add(remainingCards.remove(0));
        while (!getCardSettings(Objects.requireNonNull(getCardByUUID(discardedCards.get(discardedCards.size() - 1)))
                .getType()).canBeStartCard()) {
            discardedCards.add(remainingCards.remove(0));
        }
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
     * @param type Type of the {@link Card} that {@link CardSettings} should be returned for.
     * @return {@link CardSettings} of {@link CardType}.
     */
    private CardSettings getCardSettings(@NotNull CardType type) {
        return cardSettings.getOrDefault(type, type.getDefaultSettings());
    }

    /**
     * @return All deck cards
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public boolean playerHasCard(@NotNull UUID player, @NotNull CardColor color) {
        final List<Card> playerCards = getPlayerCards(player);
        for (Card card : playerCards)
            if (card.getType().getColor() == color)
                return true;
        return false;
    }

    /**
     * @param uuid UUID of the player.
     * @return Cards of the player.
     */
    @NotNull
    private List<Card> getPlayerCards(@NotNull UUID uuid) {
        checkPlayerExists(uuid);
        final List<UUID> playerCardsIDs = getPlayerCardUUIDs(uuid);
        final List<Card> playerCards = new ArrayList<>();
        for (UUID cardUUID : playerCardsIDs) {
            final Card card = getCardByUUID(cardUUID);
            if (card != null) {
                playerCards.add(card);
            }
        }
        return playerCards;
    }

    /**
     * @param uuid Player UUID
     * @return Player cards
     */
    @NotNull
    public List<UUID> getPlayerCardUUIDs(@NotNull UUID uuid) {
        checkPlayerExists(uuid);

        return new ArrayList<>(playersCards.get(uuid));
    }

    /**
     * @param player Player UUID
     * @param value  Card value
     * @return Whether player has a card of this value
     */
    @SuppressWarnings("unused")
    public boolean playerHasCard(@NotNull UUID player, @NotNull CardValue value) {
        final List<Card> playerCards = getPlayerCards(player);
        for (Card card : playerCards)
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
    @SuppressWarnings("unused")
    public boolean playerHasCard(@NotNull UUID player, @NotNull CardColor color, @NotNull CardValue value) {
        final List<Card> playerCards = getPlayerCards(player);
        for (Card card : playerCards)
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
    @SuppressWarnings("unused")
    public int unusedCount() {
        return remainingCards.size();
    }

    /**
     * @return Number of cards in used pile
     */
    @SuppressWarnings("unused")
    public int usedCount() {
        return discardedCards.size();
    }
}

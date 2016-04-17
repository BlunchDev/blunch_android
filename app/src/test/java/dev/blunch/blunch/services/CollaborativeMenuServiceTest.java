package dev.blunch.blunch.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.blunch.blunch.BuildConfig;
import dev.blunch.blunch.domain.CollaborativeMenu;
import dev.blunch.blunch.domain.CollaborativeMenuAnswer;
import dev.blunch.blunch.domain.Dish;
import dev.blunch.blunch.utils.MockRepository;
import dev.blunch.blunch.utils.Repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by casassg on 07/04/16.
 *
 * @author casassg
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CollaborativeMenuServiceTest {

    private CollaborativeMenuService service;
    private MockRepository<CollaborativeMenu> repository;
    private CollaborativeMenu newMenu;
    private CollaborativeMenu oldMenu;
    private Repository.OnChangedListener.EventType lastChangedType;

    @Before
    public void setUp() {
        repository = new MockRepository<>();
        service = new CollaborativeMenuService(repository);
        service = new CollaborativeMenuService(repository, new MockRepository<Dish>());
        newMenu = new CollaborativeMenu(
                "Menu de micro de la FIB",
                "Encarna", "És un menu de micro de la FIB",
                "DA FIB", new Date(10), new Date(), null, null
        );
        oldMenu = new CollaborativeMenu(
                "Menu del vertex",
                "Victor", "Tinc micros tambe",
                "Vertex", new Date(1231), new Date(), null,null
        );
        repository.insert(oldMenu);
        lastChangedType = null;
        service.setOnChangedListener(new Repository.OnChangedListener() {
            @Override
            public void onChanged(EventType type) {
                lastChangedType = type;
            }
        });
    }

    public List<CollaborativeMenu> generatDummyData(int seed, int size) {
        List<CollaborativeMenu> menus = new ArrayList<>();
        for (int i = seed; i < seed + size; ++i) {
            CollaborativeMenu menu = new CollaborativeMenu("test" + i, "test" + i, "BCN", "BCN", new Date(19 + i), new Date(), null, null);
            menus.add(menu);
        }
        return menus;
    }

    @Test
    public void onSave() {
        service.save(newMenu);

        assertEquals(2, repository.all().size());
    }

    @Test
    public void resizes(){
        assertEquals(1,service.getAmount());
        repository.insert(newMenu);
        assertEquals(2,service.getAmount());
    }

    @Test
    public void onDelete() {
        assertEquals(1,service.getAmount());
        service.delete(oldMenu.getId());
        assertEquals(0,service.getAmount());
    }

    @Test
    public void getMenu() {
        repository.insert(newMenu);
        assertEquals(newMenu,service.get(newMenu.getId()));
    }


    @Test
    public void getUnexistingMenu() {
        assertEquals(null,service.get("lasdjalksdj"));
    }

    @Test
    public void onSaveExisting() {
        assertEquals("Victor", oldMenu.getAuthor());
        String newAuthor = "Manuel";
        oldMenu.setAuthor(newAuthor);
        String oldID = oldMenu.getId();
        service.save(oldMenu);
        assertEquals(oldID, oldMenu.getId());
        assertEquals(oldID,service.get(oldMenu.getId()).getId());
        assertEquals(newAuthor,service.get(oldMenu.getId()).getAuthor());

    }

    @Test
    public void onAddExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1, service.getAmount());
        repository.simulateExternalAddition(newMenu);
        assertEquals(2, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Added,lastChangedType);
    }

    @Test
    public void onMoveExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        repository.simulateExternalMove(oldMenu);
        assertEquals(1, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Moved,lastChangedType);
    }

    @Test
    public void onChangeExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1,service.getAmount());
        assertEquals("Victor", oldMenu.getAuthor());
        String newAuthor = "Manuel";
        oldMenu.setAuthor(newAuthor);
        repository.simulateExternalChange(oldMenu);
        assertEquals(1, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Changed,lastChangedType);
        assertEquals(newAuthor,service.get(oldMenu.getId()).getAuthor());
    }

    @Test
    public void onDeleteExternal() {
        assertEquals(null, lastChangedType);
        assertEquals(1, service.getAmount());
        repository.simulateExternalDelete(oldMenu);
        assertEquals(0, service.getAmount());
        assertEquals(Repository.OnChangedListener.EventType.Removed,lastChangedType);
    }

    @Test
    public void getActiveProposals(){
        /*
        Definir CollaborativeMenuProposal (#task 2.3) hacer insert.
        Hacer get y assert.
         */
        //CollaborativeMenuAnswer answer = service.get...
        //assertNotNull(answer);
        //assertEquals(answer.getId(),id..);
        //assertEquals(answer.getMenuId,menId..);
        //assertEquals(answer.getHost,hostId..);
        //assertEquals(answer.getDate,date..);
        //int i = 0;
        //for (String s : answer.getOfferedDishes().keySet()) {
        //    assertEquals(blablablaaaaaaa.get(i).getId(), s);
        //   ++i;
        //}

    }

    @Test
    public void deleteProposalOnDecline(){
        /*
        A partir de CollaborativeMenuProposal insertada, llamar a accion
        decline q la debe borrar y hacer assert comparando si todavia existe en el sistema
         */

        //service.decline(insertada);
        //CollaborativeMenuAnswer answer = service.get....;
        //assertNull(answer);

    }

    @Test
    public void modifyCollaborativeMenuOnAccept(){
        /*
        A partir de CollaborativeProposalAnswer insertada, llamar acción "accept"
        debe modificar el menú collaborativo referenciado por la proposal para que
        incluya la propueusta hecha y debe borrar la propuesta del sistema.
        Hacer assert verificando que se ha borrado del sistema y assert sobre el
        CollaborativeMenu referenciado para ver que incluye los platos de la propuesta.
         */

        //service.accept(insertada);
        //CollaborativeMenuAnswer answer = service.get..;
        //assertNull(answer);
        //for (String s: insertada.getOfferedDishes().keySet()){
        //    assertTrue(CollaborativeMenureferenciat.contains(s));
        //}
    }


//    @Test
//    public void onSave

}

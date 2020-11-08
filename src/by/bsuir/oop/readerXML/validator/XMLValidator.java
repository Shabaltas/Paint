package by.bsuir.oop.readerXML.validator;

public class XMLValidator {
    public  <E extends Enum<E>> boolean isEnumValid(final Class<E> eClass, final String string) {
        for (E constant : eClass.getEnumConstants()) {
            if (constant.toString().equals(string)) {
                return true;
            }
        }
        return false;
    }
}

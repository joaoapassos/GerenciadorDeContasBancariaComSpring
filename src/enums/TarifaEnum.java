package enums;

import java.math.BigDecimal;

/**
 *
 * @author joaoapassos
 */

public enum TarifaEnum {
    FIXA{
        public BigDecimal aplicar(BigDecimal valor) {return valor.add(new BigDecimal(10));}
    },
    PERCENTUAL {
        public BigDecimal aplicar(BigDecimal valor) {return valor.multiply(new BigDecimal(1.01));}
    },
    ISENTA {
        public BigDecimal aplicar(BigDecimal valor) {return valor;}
    };

    public abstract BigDecimal aplicar(BigDecimal valor);
}

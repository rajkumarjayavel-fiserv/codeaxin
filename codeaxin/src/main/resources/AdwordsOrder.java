
import com.google.common.base.MoreObjects;
import com.gyft.shop.PaymentMethod;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class AdwordsOrder {
	private final Integer id;
	private final String gclid;
	private final DateTime created_when;
	private final BigDecimal card_supplier_margin;
	private final Integer gf_shop_card_id;

	protected AdwordsOrder() {
		this.id = null;
		this.gclid = null;
		this.payment_method = null;
		this.created_when = null;
		this.card_supplier_margin = null;
		this.gf_shop_card_id = null;
	}

	public Integer getId() {
		return id;
	}

	public String getGclid() {
		return gclid;
	}


	public DateTime getCreated_when() {
		return created_when;
	}

	public BigDecimal getCard_supplier_margin() {
		return card_supplier_margin;
	}

	public Integer getGf_shop_card_id() {
		return gf_shop_card_id;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", id)
				.add("gclid", gclid)
				.add("payment_method", payment_method)
				.add("created_when", created_when)
				.add("card_supplier_margin", card_supplier_margin)
				.add("gf_shop_card_id", gf_shop_card_id)
				.toString();
	}
}

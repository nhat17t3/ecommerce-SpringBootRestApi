package com.nhat.demoSpringbooRestApi.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserProductKey implements Serializable {


    private int userId;

    //	@Column(name = "product_id")
    private int productId;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + productId;
        result = prime * result + userId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserProductKey other = (UserProductKey) obj;
        if (productId != other.productId)
            return false;
        if (userId != other.userId)
            return false;
        return true;
    }
}

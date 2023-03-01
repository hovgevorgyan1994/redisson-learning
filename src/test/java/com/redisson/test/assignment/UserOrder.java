package com.redisson.test.assignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrder {
    private int id;
    private Category category;

    public UserOrder(Category category) {
        this.category = category;
    }
}

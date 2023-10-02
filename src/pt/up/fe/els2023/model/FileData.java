package pt.up.fe.els2023.model;

import java.util.Map;

public record FileData(Map<String, Object> contents, String name) {
}
